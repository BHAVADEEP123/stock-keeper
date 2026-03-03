package com.stockkeeper.app.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.stockkeeper.app.data.api.RetrofitClient
import com.stockkeeper.app.data.db.StockKeeperDatabase
import com.stockkeeper.app.data.repository.StockRepository
import com.stockkeeper.app.notification.NotificationManager
import com.stockkeeper.app.utils.NetworkUtils
import java.util.concurrent.TimeUnit

class PricePollingWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val context = applicationContext
            val database = StockKeeperDatabase.getInstance(context)
            val apiService = RetrofitClient.getStockApiService()
            val stockRepository = StockRepository(
                database.stockDao(),
                database.priceAlertDao(),
                database.pollingHistoryDao(),
                apiService
            )

            val isNetworkAvailable = NetworkUtils.isNetworkAvailable(context)

            if (!isNetworkAvailable) {
                // Network is offline, retry later
                return Result.retry()
            }

            // Get all stocks with notifications enabled
            val stocks = stockRepository.getStocksWithNotificationsEnabled()

            stocks.forEach { stock ->
                try {
                    val price = stockRepository.fetchStockPrice(stock.symbol)
                    
                    if (price != null) {
                        // Update stock price
                        val updatedStock = stock.copy(currentPrice = price)
                        stockRepository.updateStock(updatedStock)
                        
                        // Record polling history
                        stockRepository.recordPollingHistory(
                            stock.id,
                            price,
                            "SUCCESS"
                        )

                        // Check if any alerts should trigger
                        val activeAlerts = stockRepository.getActiveAlerts()
                        activeAlerts.forEach { alert ->
                            if (alert.stockId == stock.id) {
                                val shouldTrigger = when (alert.alertType) {
                                    "ABOVE" -> price >= alert.triggerPrice
                                    "BELOW" -> price <= alert.triggerPrice
                                    else -> false
                                }

                                if (shouldTrigger) {
                                    // Send notification
                                    NotificationManager.showPriceAlertNotification(
                                        context,
                                        stock,
                                        price,
                                        alert
                                    )
                                    
                                    // Mark alert as triggered
                                    val triggeredAlert = alert.copy(
                                        isTriggered = true,
                                        triggeredAt = System.currentTimeMillis()
                                    )
                                    stockRepository.updateAlert(triggeredAlert)
                                }
                            }
                        }
                    } else {
                        // Failed to fetch price
                        stockRepository.recordPollingHistory(
                            stock.id,
                            0.0,
                            "FAILED"
                        )
                    }
                } catch (e: Exception) {
                    stockRepository.recordPollingHistory(
                        stock.id,
                        0.0,
                        "FAILED"
                    )
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    companion object {
        const val POLLING_WORK_NAME = "stock_price_polling"

        fun schedulePeriodicPolling(context: Context) {
            val pollingWork = PeriodicWorkRequestBuilder<PricePollingWorker>(
                5, TimeUnit.MINUTES // Poll every 5 minutes
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                POLLING_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                pollingWork
            )
        }

        fun cancelPolling(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(POLLING_WORK_NAME)
        }
    }
}
