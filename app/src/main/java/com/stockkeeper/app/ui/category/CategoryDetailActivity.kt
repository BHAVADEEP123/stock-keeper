package com.stockkeeper.app.ui.category

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.stockkeeper.app.data.db.StockKeeperDatabase
import com.stockkeeper.app.data.repository.StockRepository
import com.stockkeeper.app.data.db.entity.Stock
import kotlinx.coroutines.launch

class CategoryDetailActivity : AppCompatActivity() {

    private lateinit var database: StockKeeperDatabase
    private lateinit var stockRepository: StockRepository
    private var categoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and repository
        database = StockKeeperDatabase.getInstance(this)
        val stockDao = database.stockDao()
        val priceAlertDao = database.priceAlertDao()
        val pollingHistoryDao = database.pollingHistoryDao()
        val apiService = com.stockkeeper.app.data.api.RetrofitClient.getStockApiService()

        stockRepository = StockRepository(
            stockDao,
            priceAlertDao,
            pollingHistoryDao,
            apiService
        )

        // Get category ID from intent
        categoryId = intent.getIntExtra("category_id", -1)

        if (categoryId == -1) {
            Toast.makeText(this, "Invalid category", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupUI()
        loadStocks()
    }

    private fun setupUI() {
        // TODO: Setup UI components for displaying stocks in this category
    }

    private fun loadStocks() {
        lifecycleScope.launch {
            // Get stocks for this category
            stockRepository.getStocksByCategory(categoryId).collect { stocks ->
                displayStocks(stocks)
            }
        }
    }

    private fun displayStocks(stocks: List<Stock>) {
        // TODO: Update UI with the list of stocks
        Toast.makeText(this, "Loaded ${stocks.size} stocks", Toast.LENGTH_SHORT).show()
    }
}

