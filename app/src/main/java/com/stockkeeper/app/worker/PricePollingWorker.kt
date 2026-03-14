package com.stockkeeper.app.worker

import android.content.Context
import androidx.work.*
import com.stockkeeper.app.data.api.RetrofitClient
import com.stockkeeper.app.data.db.StockKeeperDatabase
import com.stockkeeper.app.data.repository.StockRepository
import com.stockkeeper.app.notification.StockNotificationManager
import java.util.concurrent.TimeUnit

class PricePollingWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val db = StockKeeperDatabase.getInstance(applicationContext)
            val repo = StockRepository(
                db.sectionDao(), db.stockDao(), db.priceHistoryDao(),
                RetrofitClient.getStockApiService()
            )

            val stocks = repo.getActiveStocks()
            stocks.forEach { stock ->
                val price = repo.fetchCurrentPrice(stock.symbol, stock.exchange) ?: return@forEach
                db.stockDao().updatePrice(stock.id, price, stock.latestPrice, System.currentTimeMillis())
                repo.recordPrice(stock.id, price)

                if (stock.alarmEnabled && stock.alarmPrice != null) {
                    val triggered = when (stock.alarmType) {
                        "BUY"  -> price <= stock.alarmPrice
                        "SELL" -> price >= stock.alarmPrice
                        else   -> false
                    }
                    if (triggered) {
                        StockNotificationManager.showAlarmNotification(
                            applicationContext,
                            stock.copy(latestPrice = price)
                        )
                    }
                }
            }

            scheduleNext(applicationContext)
            Result.success()
        } catch (e: Exception) {
            scheduleNext(applicationContext)
            Result.failure()
        }
    }

    companion object {
        private const val WORK_NAME = "price_polling"

        /** WorkManager minimum is 15 min for periodic work.
         *  We use self-chaining OneTimeWorkRequests to achieve ~5-min polling. */
        fun scheduleNext(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val work = OneTimeWorkRequestBuilder<PricePollingWorker>()
                .setInitialDelay(5, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, work)
        }

        fun startNow(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val work = OneTimeWorkRequestBuilder<PricePollingWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, work)
        }
    }
}
