package com.stockkeeper.app.notification

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.stockkeeper.app.R
import com.stockkeeper.app.data.db.entity.StockEntity
import com.stockkeeper.app.ui.MainActivity
import kotlin.random.Random

object StockNotificationManager {
    private const val CHANNEL_ID   = "stock_alarms"
    private const val CHANNEL_NAME = "Stock Price Alarms"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Triggered when a stock hits your target price" }
            (context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as android.app.NotificationManager).createNotificationChannel(channel)
        }
    }

    fun showAlarmNotification(context: Context, stock: StockEntity) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as android.app.NotificationManager

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("stock_id", stock.id)
        }
        val pi = PendingIntent.getActivity(
            context, stock.id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val name   = stock.displayName.ifBlank { stock.symbol }
        val action = if (stock.alarmType == "BUY") "BUY opportunity" else "SELL opportunity"
        val price  = "₹%.2f".format(stock.latestPrice)
        val target = "₹%.2f".format(stock.alarmPrice ?: 0.0)
        val body   = "${stock.exchange}: $price has reached your $action target of $target"

        nm.notify(
            Random.nextInt(),
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("$name — $action")
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .build()
        )
    }
}
