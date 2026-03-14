package com.stockkeeper.app.ui.alarms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.stockkeeper.app.data.db.entity.SectionEntity
import com.stockkeeper.app.data.db.entity.StockEntity
import com.stockkeeper.app.data.db.entity.PriceHistoryEntity
import com.stockkeeper.app.data.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlarmsViewModel(val repository: StockRepository) : ViewModel() {

    val sections: StateFlow<List<SectionEntity>> = repository.getSections()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun getStocksForSection(sectionId: Int): Flow<List<StockEntity>> =
        repository.getStocksBySection(sectionId)

    fun getStockFlow(stockId: Int): Flow<StockEntity?> =
        repository.getStockByIdFlow(stockId)

    fun getPriceHistory(stockId: Int): Flow<List<PriceHistoryEntity>> =
        repository.getPriceHistoryToday(stockId)

    fun addSection(name: String) {
        viewModelScope.launch { repository.insertSection(SectionEntity(name = name)) }
    }

    fun deleteSection(section: SectionEntity) {
        viewModelScope.launch { repository.deleteSection(section) }
    }

    fun addStock(stock: StockEntity) {
        viewModelScope.launch { repository.insertStock(stock) }
    }

    fun updateStock(stock: StockEntity) {
        viewModelScope.launch { repository.updateStock(stock) }
    }

    fun deleteStock(stock: StockEntity) {
        viewModelScope.launch { repository.deleteStock(stock) }
    }

    suspend fun getSectionById(id: Int): SectionEntity? = repository.getSectionById(id)

    class Factory(private val repository: StockRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AlarmsViewModel(repository) as T
    }
}
