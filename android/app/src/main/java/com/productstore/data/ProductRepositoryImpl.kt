package com.productstore.data

import com.productstore.data.localDataSource.ProductDao
import com.productstore.data.localDataSource.ProductEntity
import com.productstore.data.remoteDataSource.ApiInterface
import com.productstore.domain.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val apiInterface: ApiInterface
) : ProductRepository {
    override fun fetchProductLists(): Flow<List<ProductEntity>> {
        return flow {
            val response = apiInterface.fetchProduct()
            productDao.deleteProducts()
            productDao.insertProducts(response)
            val list = productDao.getProductList()
            emit(list)
        }.flowOn(Dispatchers.IO)
    }

    override fun updateProduct(product: ProductEntity): Flow<Boolean> {
        return flow {
            val response = if (product.id == -1) {
                apiInterface.createProduct(product)
            } else {
                apiInterface.updateProduct(product.id, product)
            }
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}
