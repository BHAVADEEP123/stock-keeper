package com.stockkeeper.app.data.db.dao

import androidx.room.*
import com.stockkeeper.app.data.db.entity.Category
import com.stockkeeper.app.data.db.entity.Stock
import com.stockkeeper.app.data.db.entity.PriceAlert
import com.stockkeeper.app.data.db.entity.PollingHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category?
}

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: Stock): Long

    @Update
    suspend fun updateStock(stock: Stock)

    @Delete
    suspend fun deleteStock(stock: Stock)

    @Query("SELECT * FROM stocks WHERE categoryId = :categoryId ORDER BY name ASC")
    fun getStocksByCategory(categoryId: Int): Flow<List<Stock>>

    @Query("SELECT * FROM stocks WHERE id = :id")
    suspend fun getStockById(id: Int): Stock?

    @Query("SELECT * FROM stocks ORDER BY name ASC")
    fun getAllStocks(): Flow<List<Stock>>

    @Query("SELECT * FROM stocks WHERE notificationEnabled = 1")
    suspend fun getStocksWithNotificationsEnabled(): List<Stock>
}

@Dao
interface PriceAlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: PriceAlert): Long

    @Update
    suspend fun updateAlert(alert: PriceAlert)

    @Delete
    suspend fun deleteAlert(alert: PriceAlert)

    @Query("SELECT * FROM price_alerts WHERE stockId = :stockId")
    fun getAlertsByStock(stockId: Int): Flow<List<PriceAlert>>

    @Query("SELECT * FROM price_alerts WHERE stockId = :stockId AND isTriggered = 0")
    suspend fun getActiveAlertsByStock(stockId: Int): List<PriceAlert>

    @Query("SELECT * FROM price_alerts WHERE isTriggered = 0")
    suspend fun getAllActiveAlerts(): List<PriceAlert>
}

@Dao
interface PollingHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPollingRecord(history: PollingHistory): Long

    @Query("SELECT * FROM polling_history WHERE stockId = :stockId ORDER BY timestamp DESC")
    suspend fun getPollingHistoryByStock(stockId: Int): List<PollingHistory>

    @Query("SELECT * FROM polling_history ORDER BY timestamp DESC LIMIT 100")
    suspend fun getRecentPollingHistory(): List<PollingHistory>

    @Query("DELETE FROM polling_history WHERE timestamp < :olderThanTimestamp")
    suspend fun deleteOldRecords(olderThanTimestamp: Long)
}
