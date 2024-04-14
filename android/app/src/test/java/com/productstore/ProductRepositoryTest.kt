package com.productstore

import com.productstore.data.ProductRepositoryImpl
import com.productstore.data.localDataSource.ProductDao
import com.productstore.data.localDataSource.ProductEntity
import com.productstore.data.remoteDataSource.ApiInterface
import com.productstore.domain.ProductRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ProductRepositoryTest {

    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var productDao: ProductDao

    @Mock
    private lateinit var apiInterface: ApiInterface

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        productRepository = ProductRepositoryImpl(productDao, apiInterface)
    }

    @Test
    fun fetchProductLists_test() {
        runBlocking {
            `when`(apiInterface.fetchProduct()).thenReturn(
                listOf(
                    ProductEntity(
                        1,
                        "p1",
                        "d1",
                        "",
                        1.1,
                        "c1"
                    ),
                    ProductEntity(
                        2,
                        "p2",
                        "d3",
                        "",
                        2.2,
                        "c3"
                    ),
                )
            )
            `when`(productDao.getProductList()).thenReturn(
                listOf(
                    ProductEntity(
                        1,
                        "p1",
                        "d1",
                        "",
                        1.1,
                        "c1"
                    ),
                    ProductEntity(
                        2,
                        "p2",
                        "d3",
                        "",
                        2.2,
                        "c3"
                    ),
                )
            )

            val actual = productRepository.fetchProductLists().last()
            val expected = 2
            assertEquals(expected, actual.size)
        }
    }

    @Test
    fun updateProduct_test_update() {
        runBlocking {
            val productEntity = ProductEntity(
                1,
                "p1",
                "d1",
                "",
                1.1,
                "c1"
            )
            `when`(apiInterface.updateProduct(productEntity.id, productEntity)).thenReturn(
                true
            )

            val actual = productRepository.updateProduct(productEntity).first()
            val expected = true
            assertEquals(expected, actual)
        }
    }

    @Test
    fun updateProduct_test_create() {
        runBlocking {
            val productEntity = ProductEntity(
                -1,
                "p1",
                "d1",
                "",
                1.1,
                "c1"
            )
            `when`(apiInterface.createProduct(productEntity)).thenReturn(
                true
            )

            val actual = productRepository.updateProduct(productEntity).first()
            val expected = true
            assertEquals(actual, expected)
        }
    }

}