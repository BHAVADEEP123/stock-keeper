package com.stockkeeper.app.data.db.dao

import androidx.room.*
import com.stockkeeper.app.data.db.entity.SectionEntity
import com.stockkeeper.app.data.db.entity.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSection(section: SectionEntity): Long

    @Update
    suspend fun updateSection(section: SectionEntity)

    @Delete
    suspend fun deleteSection(section: SectionEntity)

    @Query("SELECT * FROM sections ORDER BY sortOrder ASC, name ASC")
    fun getAllSections(): Flow<List<SectionEntity>>

    @Query("SELECT * FROM sections WHERE id = :id")
    suspend fun getSectionById(id: Int): SectionEntity?
}

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: StockEntity): Long
    
    @Update
    suspend fun updateStock(stock: StockEntity)
    
    @Delete
    suspend fun deleteStock(stock: StockEntity)
    
    @Query("SELECT * FROM stocks WHERE sectionId = :sectionId ORDER BY displayName ASC, symbol ASC")
    fun getStocksBySection(sectionId: Int): Flow<List<StockEntity>>
    
    @Query("SELECT * FROM stocks WHERE id = :id")
    suspend fun getStockById(id: Int): StockEntity?

    @Query("SELECT * FROM stocks ORDER BY displayName ASC, symbol ASC")
    fun getAllStocks(): Flow<List<StockEntity>>

    @Query("SELECT * FROM stocks WHERE isActive = 1")
    suspend fun getActiveStocks(): List<StockEntity>
    
    @Query("SELECT * FROM stocks WHERE isActive = 1 AND alertPrice IS NOT NULL")
    suspend fun getStocksWithActiveAlerts(): List<StockEntity>
}
