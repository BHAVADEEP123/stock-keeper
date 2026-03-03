package com.stockkeeper.app.data.repository

import com.stockkeeper.app.data.api.StockApiService
import com.stockkeeper.app.data.db.dao.StockDao
import com.stockkeeper.app.data.db.dao.PriceAlertDao
import com.stockkeeper.app.data.db.dao.PollingHistoryDao
import com.stockkeeper.app.data.db.entity.Stock
import com.stockkeeper.app.data.db.entity.PriceAlert
import com.stockkeeper.app.data.db.entity.PollingHistory
import kotlinx.coroutines.flow.Flow

class StockRepository(
    private val stockDao: StockDao,
    private val priceAlertDao: PriceAlertDao,
    private val pollingHistoryDao: PollingHistoryDao,
    private val apiService: StockApiService
) {

    fun getStocksByCategory(categoryId: Int): Flow<List<Stock>> = 
        stockDao.getStocksByCategory(categoryId)

    fun getAllStocks(): Flow<List<Stock>> = stockDao.getAllStocks()

    suspend fun getStockById(id: Int): Stock? = stockDao.getStockById(id)

    suspend fun insertStock(stock: Stock): Long = stockDao.insertStock(stock)

    suspend fun updateStock(stock: Stock) = stockDao.updateStock(stock)

    suspend fun deleteStock(stock: Stock) = stockDao.deleteStock(stock)

    suspend fun getStocksWithNotificationsEnabled(): List<Stock> = 
        stockDao.getStocksWithNotificationsEnabled()

    suspend fun fetchStockPrice(symbol: String): Double? {
        return try {
            val response = apiService.getStockPrice(symbol)
            response.quote.price.toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun recordPollingHistory(
        stockId: Int,
        price: Double,
        status: String
    ) {
        pollingHistoryDao.insertPollingRecord(
            PollingHistory(
                stockId = stockId,
                price = price,
                status = status
            )
        )
    }

    suspend fun getActiveAlerts(): List<PriceAlert> = priceAlertDao.getAllActiveAlerts()

    suspend fun updateAlert(alert: PriceAlert) = priceAlertDao.updateAlert(alert)

    suspend fun insertAlert(alert: PriceAlert): Long = priceAlertDao.insertAlert(alert)

    fun getAlertsByStock(stockId: Int): Flow<List<PriceAlert>> = 
        priceAlertDao.getAlertsByStock(stockId)

    suspend fun deleteAlert(alert: PriceAlert) = priceAlertDao.deleteAlert(alert)
}
