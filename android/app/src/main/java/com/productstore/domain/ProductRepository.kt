package com.productstore.domain

import com.productstore.data.localDataSource.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun fetchProductLists(): Flow<List<ProductEntity>>

    fun updateProduct(product: ProductEntity): Flow<Boolean>
}
