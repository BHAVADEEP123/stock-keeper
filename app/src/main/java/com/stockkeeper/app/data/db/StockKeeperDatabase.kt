package com.stockkeeper.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.stockkeeper.app.data.db.dao.CategoryDao
import com.stockkeeper.app.data.db.dao.StockDao
import com.stockkeeper.app.data.db.dao.PriceAlertDao
import com.stockkeeper.app.data.db.dao.PollingHistoryDao
import com.stockkeeper.app.data.db.entity.Category
import com.stockkeeper.app.data.db.entity.Stock
import com.stockkeeper.app.data.db.entity.PriceAlert
import com.stockkeeper.app.data.db.entity.PollingHistory

@Database(
    entities = [Category::class, Stock::class, PriceAlert::class, PollingHistory::class],
    version = 1,
    exportSchema = false
)
abstract class StockKeeperDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun stockDao(): StockDao
    abstract fun priceAlertDao(): PriceAlertDao
    abstract fun pollingHistoryDao(): PollingHistoryDao

    companion object {
        @Volatile
        private var Instance: StockKeeperDatabase? = null

        fun getInstance(context: Context): StockKeeperDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    StockKeeperDatabase::class.java,
                    "stock_keeper.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
