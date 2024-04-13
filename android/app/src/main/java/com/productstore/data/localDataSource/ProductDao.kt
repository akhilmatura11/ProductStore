package com.productstore.data.localDataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("Select * from ProductEntity")
    fun getProductList(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(productList: List<ProductEntity>)

    @Query("Delete from ProductEntity")
    fun deleteProducts()
}
