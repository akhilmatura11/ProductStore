package com.productstore.data.localDataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        private lateinit var appDatabase: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            if (!Companion::appDatabase.isInitialized) {
                appDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ProductStore"
                ).build()
            }

            return appDatabase
        }
    }
}
