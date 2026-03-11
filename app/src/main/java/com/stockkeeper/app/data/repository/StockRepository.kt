package com.stockkeeper.app.data.repository

import com.stockkeeper.app.data.api.StockApiService
import com.stockkeeper.app.data.db.dao.StockDao
import com.stockkeeper.app.data.db.entity.StockEntity
import kotlinx.coroutines.flow.Flow

class StockRepository(
    private val stockDao: StockDao,
    private val apiService: StockApiService
) {

    fun getStocksBySection(sectionId: Int): Flow<List<StockEntity>> =
        stockDao.getStocksBySection(sectionId)

    fun getAllStocks(): Flow<List<StockEntity>> = stockDao.getAllStocks()

    suspend fun getStockById(id: Int): StockEntity? = stockDao.getStockById(id)

    suspend fun insertStock(stock: StockEntity): Long = stockDao.insertStock(stock)

    suspend fun updateStock(stock: StockEntity) = stockDao.updateStock(stock)

    suspend fun deleteStock(stock: StockEntity) = stockDao.deleteStock(stock)

    suspend fun getActiveStocks(): List<StockEntity> =
        stockDao.getActiveStocks()

    suspend fun fetchStockPrice(symbol: String): Double? {
        return try {
            val response = apiService.getStockPrice(symbol)
            response.quote.price.toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getStocksWithActiveAlerts(): List<StockEntity> =
        stockDao.getStocksWithActiveAlerts()
}
