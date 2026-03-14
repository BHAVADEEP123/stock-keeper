package com.stockkeeper.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.stockkeeper.app.data.db.dao.PriceHistoryDao
import com.stockkeeper.app.data.db.dao.SectionDao
import com.stockkeeper.app.data.db.dao.StockDao
import com.stockkeeper.app.data.db.entity.PriceHistoryEntity
import com.stockkeeper.app.data.db.entity.SectionEntity
import com.stockkeeper.app.data.db.entity.StockEntity

@Database(
    entities = [SectionEntity::class, StockEntity::class, PriceHistoryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class StockKeeperDatabase : RoomDatabase() {
    abstract fun sectionDao(): SectionDao
    abstract fun stockDao(): StockDao
    abstract fun priceHistoryDao(): PriceHistoryDao

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
