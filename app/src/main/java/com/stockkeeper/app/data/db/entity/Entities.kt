package com.stockkeeper.app.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "sections")
data class SectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "stocks",
    foreignKeys = [ForeignKey(
        entity = SectionEntity::class,
        parentColumns = ["id"],
        childColumns = ["sectionId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("sectionId")]
)
data class StockEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sectionId: Int,
    val symbol: String,
    val exchange: String,           // "NSE" or "BSE"
    val displayName: String = "",
    val latestPrice: Double = 0.0,
    val previousPrice: Double = 0.0,
    val lastUpdated: Long = 0L,
    val alarmEnabled: Boolean = false,
    val alarmType: String? = null,  // "BUY" or "SELL"
    val alarmPrice: Double? = null,
    val isActive: Boolean = true
)

@Entity(
    tableName = "price_history",
    foreignKeys = [ForeignKey(
        entity = StockEntity::class,
        parentColumns = ["id"],
        childColumns = ["stockId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("stockId")]
)
data class PriceHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val stockId: Int,
    val price: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val date: String  // "YYYY-MM-DD" used for daily purge
)
