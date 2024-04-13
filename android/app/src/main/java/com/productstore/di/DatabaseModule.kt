package com.productstore.di

import android.content.Context
import com.productstore.data.localDataSource.AppDatabase
import com.productstore.data.localDataSource.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun getAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun getProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao()
    }
}
