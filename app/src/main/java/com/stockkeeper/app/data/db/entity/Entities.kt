package com.stockkeeper.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "stocks")
data class Stock(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryId: Int,
    val name: String,
    val symbol: String,
    val buyPrice: Double,
    val currentPrice: Double = 0.0,
    val quantity: Int = 1,
    val notificationEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "price_alerts")
data class PriceAlert(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val stockId: Int,
    val triggerPrice: Double,
    val alertType: String, // "ABOVE" or "BELOW"
    val isTriggered: Boolean = false,
    val triggeredAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "polling_history")
data class PollingHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val stockId: Int,
    val price: Double,
    val status: String, // "SUCCESS", "FAILED", "NO_NETWORK"
    val timestamp: Long = System.currentTimeMillis()
)
