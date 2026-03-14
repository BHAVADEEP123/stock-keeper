package com.stockkeeper.app.data.db.dao

import androidx.room.*
import com.stockkeeper.app.data.db.entity.PriceHistoryEntity
import com.stockkeeper.app.data.db.entity.SectionEntity
import com.stockkeeper.app.data.db.entity.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(section: SectionEntity): Long

    @Update
    suspend fun update(section: SectionEntity)

    @Delete
    suspend fun delete(section: SectionEntity)

    @Query("SELECT * FROM sections ORDER BY sortOrder ASC, name ASC")
    fun getAll(): Flow<List<SectionEntity>>

    @Query("SELECT * FROM sections WHERE id = :id")
    suspend fun getById(id: Int): SectionEntity?
}

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stock: StockEntity): Long

    @Update
    suspend fun update(stock: StockEntity)

    @Delete
    suspend fun delete(stock: StockEntity)

    @Query("SELECT * FROM stocks WHERE sectionId = :sectionId ORDER BY displayName ASC, symbol ASC")
    fun getBySection(sectionId: Int): Flow<List<StockEntity>>

    @Query("SELECT * FROM stocks WHERE id = :id")
    suspend fun getById(id: Int): StockEntity?

    @Query("SELECT * FROM stocks WHERE id = :id")
    fun getByIdFlow(id: Int): Flow<StockEntity?>

    @Query("SELECT * FROM stocks ORDER BY displayName ASC, symbol ASC")
    fun getAll(): Flow<List<StockEntity>>

    @Query("SELECT * FROM stocks WHERE isActive = 1")
    suspend fun getActive(): List<StockEntity>

    @Query("SELECT * FROM stocks WHERE isActive = 1 AND alarmEnabled = 1 AND alarmPrice IS NOT NULL")
    suspend fun getWithActiveAlarms(): List<StockEntity>

    @Query("UPDATE stocks SET latestPrice = :price, previousPrice = :prev, lastUpdated = :ts WHERE id = :id")
    suspend fun updatePrice(id: Int, price: Double, prev: Double, ts: Long)
}

@Dao
interface PriceHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: PriceHistoryEntity): Long

    @Query("SELECT * FROM price_history WHERE stockId = :stockId AND date = :date ORDER BY timestamp ASC")
    fun getForDayFlow(stockId: Int, date: String): Flow<List<PriceHistoryEntity>>

    @Query("DELETE FROM price_history WHERE date != :today")
    suspend fun purgeOld(today: String)
}
