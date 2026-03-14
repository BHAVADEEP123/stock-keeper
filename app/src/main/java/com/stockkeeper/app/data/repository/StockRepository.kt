package com.stockkeeper.app.data.repository

import com.stockkeeper.app.data.api.StockApiService
import com.stockkeeper.app.data.db.dao.PriceHistoryDao
import com.stockkeeper.app.data.db.dao.SectionDao
import com.stockkeeper.app.data.db.dao.StockDao
import com.stockkeeper.app.data.db.entity.PriceHistoryEntity
import com.stockkeeper.app.data.db.entity.SectionEntity
import com.stockkeeper.app.data.db.entity.StockEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StockRepository(
    val sectionDao: SectionDao,
    val stockDao: StockDao,
    val priceHistoryDao: PriceHistoryDao,
    private val apiService: StockApiService
) {

    // ── Sections ──────────────────────────────────────────────────────────────
    fun getSections(): Flow<List<SectionEntity>> = sectionDao.getAll()

    suspend fun insertSection(s: SectionEntity): Long = sectionDao.insert(s)

    suspend fun updateSection(s: SectionEntity) = sectionDao.update(s)

    suspend fun deleteSection(s: SectionEntity) = sectionDao.delete(s)

    suspend fun getSectionById(id: Int): SectionEntity? = sectionDao.getById(id)

    // ── Stocks ────────────────────────────────────────────────────────────────
    fun getStocksBySection(sectionId: Int): Flow<List<StockEntity>> =
        stockDao.getBySection(sectionId)

    fun getStockByIdFlow(id: Int): Flow<StockEntity?> = stockDao.getByIdFlow(id)

    suspend fun getStockById(id: Int): StockEntity? = stockDao.getById(id)

    suspend fun insertStock(s: StockEntity): Long = stockDao.insert(s)

    suspend fun updateStock(s: StockEntity) = stockDao.update(s)

    suspend fun deleteStock(s: StockEntity) = stockDao.delete(s)

    suspend fun getActiveStocks(): List<StockEntity> = stockDao.getActive()

    // ── Price history ─────────────────────────────────────────────────────────
    fun getPriceHistoryToday(stockId: Int): Flow<List<PriceHistoryEntity>> =
        priceHistoryDao.getForDayFlow(stockId, today())

    suspend fun recordPrice(stockId: Int, price: Double) {
        priceHistoryDao.insert(PriceHistoryEntity(stockId = stockId, price = price, date = today()))
    }

    suspend fun purgeOldHistory() = priceHistoryDao.purgeOld(today())

    // ── Yahoo Finance API ─────────────────────────────────────────────────────
    // NSE -> SYMBOL.NS  |  BSE -> SYMBOL.BO
    private fun yahooSymbol(symbol: String, exchange: String): String =
        when (exchange.uppercase()) {
            "NSE" -> "${symbol.uppercase()}.NS"
            "BSE" -> "${symbol.uppercase()}.BO"
            else  -> symbol.uppercase()
        }

    suspend fun fetchCurrentPrice(symbol: String, exchange: String): Double? {
        return try {
            val resp = apiService.getQuote(
                yahooSymbol(symbol, exchange),
                interval = "1d",
                range = "1d"
            )
            resp.chart.result?.firstOrNull()?.meta?.regularMarketPrice?.takeIf { it > 0 }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchIntradayPrices(symbol: String, exchange: String): List<Pair<Long, Double>> {
        return try {
            val resp = apiService.getQuote(
                yahooSymbol(symbol, exchange),
                interval = "5m",
                range = "1d"
            )
            val result = resp.chart.result?.firstOrNull() ?: return emptyList()
            val timestamps = result.timestamp ?: return emptyList()
            val closes = result.indicators?.quote?.firstOrNull()?.close ?: return emptyList()
            timestamps.zip(closes)
                .filter { (_, p) -> p != null && p > 0 }
                .map { (ts, p) -> Pair(ts * 1000L, p!!) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ── Utils ─────────────────────────────────────────────────────────────────
    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}
