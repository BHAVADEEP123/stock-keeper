package com.stockkeeper.app

import android.app.Application
import com.stockkeeper.app.notification.StockNotificationManager
import com.stockkeeper.app.worker.DailyCleanupWorker
import com.stockkeeper.app.worker.PricePollingWorker

class StockKeeperApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        StockNotificationManager.createNotificationChannel(this)
        PricePollingWorker.startNow(this)
        DailyCleanupWorker.schedule(this)
    }
}
