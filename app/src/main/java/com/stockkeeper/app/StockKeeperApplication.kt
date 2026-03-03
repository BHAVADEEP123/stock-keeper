package com.stockkeeper.app

import android.app.Application
import com.stockkeeper.app.worker.PricePollingWorker

class StockKeeperApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize polling when app starts
        PricePollingWorker.schedulePeriodicPolling(this)
    }
}
