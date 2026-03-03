package com.stockkeeper.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.stockkeeper.app.utils.NetworkUtils
import com.stockkeeper.app.worker.PricePollingWorker
import androidx.work.WorkManager

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetworkUtils.isNetworkAvailable(context)
        
        if (isConnected) {
            // Network is back online, resume polling
            PricePollingWorker.schedulePeriodicPolling(context)
        } else {
            // Network is offline, you can optionally cancel polling
            // or just let it retry when network is available
        }
    }
}
