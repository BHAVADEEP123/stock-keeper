package com.stockkeeper.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.stockkeeper.app.data.api.RetrofitClient
import com.stockkeeper.app.data.api.StockApiService
import com.stockkeeper.app.data.db.StockKeeperDatabase
import com.stockkeeper.app.data.db.entity.SectionEntity
import com.stockkeeper.app.data.db.entity.StockEntity
import com.stockkeeper.app.data.db.dao.SectionDao
import com.stockkeeper.app.data.db.dao.StockDao
import com.stockkeeper.app.data.repository.StockRepository
import com.stockkeeper.app.notification.NotificationManager
import com.stockkeeper.app.ui.screens.SectionDetailScreen
import com.stockkeeper.app.ui.screens.SectionsScreen
import com.stockkeeper.app.ui.screens.StockEditorScreen
import com.stockkeeper.app.ui.theme.StockKeeperTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory(
            database = StockKeeperDatabase.getInstance(this),
            apiService = RetrofitClient.getStockApiService()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationManager.createNotificationChannel(this)

        setContent {
            StockKeeperApp(viewModel)
        }
    }
}

class MainViewModel(
    private val sectionDao: SectionDao,
    private val stockDao: StockDao,
    val stockRepository: StockRepository
) : ViewModel() {

    val sections: Flow<List<SectionEntity>> = sectionDao.getAllSections()

    fun getStocksForSection(sectionId: Int): Flow<List<StockEntity>> =
        stockRepository.getStocksBySection(sectionId)

    fun insertSection(name: String) {
        viewModelScope.launch {
            val order = (sections as? Flow<List<SectionEntity>>)?.let { 0 } ?: 0
            sectionDao.insertSection(
                SectionEntity(
                    name = name,
                    sortOrder = order
                )
            )
        }
    }

    fun insertOrUpdateStock(stock: StockEntity) {
        viewModelScope.launch {
            if (stock.id == 0) {
                stockRepository.insertStock(stock)
            } else {
                stockRepository.updateStock(stock)
            }
        }
    }

    fun deleteStock(stock: StockEntity) {
        viewModelScope.launch {
            stockRepository.deleteStock(stock)
        }
    }

    suspend fun getStockById(id: Int): StockEntity? = stockRepository.getStockById(id)

    class Factory(
        private val database: StockKeeperDatabase,
        private val apiService: StockApiService
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val sectionDao = database.sectionDao()
            val stockDao = database.stockDao()
            val stockRepository = StockRepository(stockDao, apiService)
            return MainViewModel(sectionDao, stockDao, stockRepository) as T
        }
    }
}

@Composable
fun StockKeeperApp(viewModel: MainViewModel) {
    StockKeeperTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "sections"
            ) {
                composable("sections") {
                    SectionsScreen(
                        viewModel = viewModel,
                        onSectionClick = { sectionId ->
                            navController.navigate("section/$sectionId")
                        },
                        onAddSection = { name ->
                            viewModel.insertSection(name)
                        }
                    )
                }
                composable(
                    route = "section/{sectionId}",
                    arguments = listOf(
                        navArgument("sectionId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val sectionId = backStackEntry.arguments?.getInt("sectionId") ?: 0
                    SectionDetailScreen(
                        sectionId = sectionId,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onEditStock = { stockId ->
                            navController.navigate("stockEditor?sectionId=$sectionId&stockId=$stockId")
                        },
                        onAddStock = {
                            navController.navigate("stockEditor?sectionId=$sectionId")
                        }
                    )
                }
                composable(
                    route = "stockEditor?sectionId={sectionId}&stockId={stockId}",
                    arguments = listOf(
                        navArgument("sectionId") {
                            type = NavType.IntType
                            defaultValue = 0
                        },
                        navArgument("stockId") {
                            type = NavType.IntType
                            defaultValue = 0
                        }
                    )
                ) { backStackEntry ->
                    val sectionId = backStackEntry.arguments?.getInt("sectionId") ?: 0
                    val stockId = backStackEntry.arguments?.getInt("stockId") ?: 0
                    StockEditorScreen(
                        sectionId = sectionId,
                        stockId = stockId,
                        viewModel = viewModel,
                        onDone = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
