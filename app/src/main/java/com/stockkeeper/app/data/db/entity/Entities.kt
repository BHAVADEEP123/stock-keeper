package com.stockkeeper.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sections")
data class SectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sectionId: Int,
    val symbol: String,
    val exchange: String, // "NSE" or "BSE"
    val displayName: String = "",
    val latestPrice: Double = 0.0,
    val previousPrice: Double = 0.0,
    val lastUpdated: Long = 0L,
    val alertPrice: Double? = null,
    val alertDirection: String? = null, // "ABOVE" or "BELOW"
    val isActive: Boolean = true
)
