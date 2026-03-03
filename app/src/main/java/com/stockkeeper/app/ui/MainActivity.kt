package com.stockkeeper.app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.stockkeeper.app.databinding.ActivityMainBinding
import com.stockkeeper.app.data.db.StockKeeperDatabase
import com.stockkeeper.app.data.repository.CategoryRepository
import com.stockkeeper.app.data.db.entity.Category
import com.stockkeeper.app.notification.NotificationManager as StockNotificationManager
import com.stockkeeper.app.worker.PricePollingWorker
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var database: StockKeeperDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database and repository
        database = StockKeeperDatabase.getInstance(this)
        categoryRepository = CategoryRepository(database.categoryDao())

        // Create notification channel
        StockNotificationManager.createNotificationChannel(this)

        // Schedule periodic polling
        PricePollingWorker.schedulePeriodicPolling(this)

        setupUI()
        observeCategories()
    }

    private fun setupUI() {
        binding.btnAddCategory.setOnClickListener {
            showAddCategoryDialog()
        }
    }

    private fun observeCategories() {
        lifecycleScope.launch {
            categoryRepository.getAllCategories().collect { categories ->
                updateCategoryList(categories)
            }
        }
    }

    private fun updateCategoryList(categories: List<Category>) {
        // TODO: Update RecyclerView with categories
        binding.tvCategoryCount.text = "Categories: ${categories.size}"
    }

    private fun showAddCategoryDialog() {
        // TODO: Implement dialog for adding category
        val category = Category(
            name = "Sample Category",
            description = "Sample Description"
        )
        
        lifecycleScope.launch {
            categoryRepository.insertCategory(category)
            Toast.makeText(this@MainActivity, "Category added", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Polling will continue in background via WorkManager
    }
}
