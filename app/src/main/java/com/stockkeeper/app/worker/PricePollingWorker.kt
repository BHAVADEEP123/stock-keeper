package com.stockkeeper.app.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.stockkeeper.app.data.api.RetrofitClient
import com.stockkeeper.app.data.db.StockKeeperDatabase
import com.stockkeeper.app.data.repository.StockRepository
import com.stockkeeper.app.notification.NotificationManager
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
                apiService
            )
            // Get all active stocks
            val stocks = stockRepository.getActiveStocks()

            stocks.forEach { stock ->
                val price = stockRepository.fetchStockPrice(stock.symbol)

                if (price != null) {
                    val updatedStock = stock.copy(
                        previousPrice = stock.latestPrice,
                        latestPrice = price,
                        lastUpdated = System.currentTimeMillis()
                    )
                    stockRepository.updateStock(updatedStock)

                    if (updatedStock.alertPrice != null && updatedStock.alertDirection != null) {
                        val shouldTrigger = when (updatedStock.alertDirection) {
                            "ABOVE" -> updatedStock.latestPrice >= updatedStock.alertPrice
                            "BELOW" -> updatedStock.latestPrice <= updatedStock.alertPrice
                            else -> false
                        }

                        if (shouldTrigger) {
                            NotificationManager.showPriceAlertNotification(
                                context,
                                updatedStock
                            )
                        }
                    }
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
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val pollingWork = PeriodicWorkRequestBuilder<PricePollingWorker>(
                30, TimeUnit.MINUTES // Poll every 30 minutes
            )
                .setConstraints(constraints)
                .build()

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
