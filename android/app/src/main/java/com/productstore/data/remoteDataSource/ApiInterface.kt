package com.productstore.data.remoteDataSource

import com.productstore.data.localDataSource.ProductEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {

    @GET("/products")
    suspend fun fetchProduct(): List<ProductEntity>

    @PUT("/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body productEntity: ProductEntity
    ): Boolean

    @POST("/products")
    suspend fun createProduct(
        @Body productEntity: ProductEntity
    ): Boolean
}
