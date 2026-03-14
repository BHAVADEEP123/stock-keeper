package com.stockkeeper.app.worker

import android.content.Context
import androidx.work.*
import com.stockkeeper.app.data.api.RetrofitClient
import com.stockkeeper.app.data.db.StockKeeperDatabase
import com.stockkeeper.app.data.repository.StockRepository
import java.util.concurrent.TimeUnit

class DailyCleanupWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val db = StockKeeperDatabase.getInstance(applicationContext)
            StockRepository(
                db.sectionDao(), db.stockDao(), db.priceHistoryDao(),
                RetrofitClient.getStockApiService()
            ).purgeOldHistory()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        private const val WORK_NAME = "daily_cleanup"

        fun schedule(context: Context) {
            val work = PeriodicWorkRequestBuilder<DailyCleanupWorker>(1, TimeUnit.DAYS).build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, work
            )
        }
    }
}
