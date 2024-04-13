package com.productstore.di

import com.productstore.data.ProductRepositoryImpl
import com.productstore.data.localDataSource.ProductDao
import com.productstore.data.remoteDataSource.ApiInterface
import com.productstore.domain.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun getRepository(productDao: ProductDao, apiInterface: ApiInterface): ProductRepository {
        return ProductRepositoryImpl(productDao, apiInterface)
    }
}
