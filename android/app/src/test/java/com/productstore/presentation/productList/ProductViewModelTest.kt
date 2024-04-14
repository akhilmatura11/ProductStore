package com.productstore.presentation.productList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.productstore.MainDispatcherRule
import com.productstore.data.localDataSource.ProductEntity
import com.productstore.domain.ProductRepository
import com.productstore.domain.ResponseStatus
import com.productstore.domain.SortType
import com.productstore.domain.UpdateType
import com.productstore.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class ProductViewModelTest {

    @get:Rule
    var instantExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var productRepository: ProductRepository

    private lateinit var productViewModel: ProductViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        productViewModel = ProductViewModel(productRepository)
    }

    @Test
    fun fetchProductLists_test() {
        val expected = true
        whenever(productRepository.fetchProductLists())
            .thenReturn(flow {
                emit(
                    listOf(
                        ProductEntity(
                            1,
                            "p1",
                            "d1",
                            "",
                            1.1,
                            "c1"
                        )
                    )
                )
            })

        productViewModel.fetchProducts()

        val responseStatus = productViewModel.responseStatus.getOrAwaitValue()
        assertEquals(expected, responseStatus is ResponseStatus.Success<*>)
    }

    @Test
    fun validateData_test_update() {
        val oldProduct = ProductEntity(0, "name", "description", "", 2.0, "category")
        val newProduct = ProductEntity(1, "name", "description", "", 1.0, "category")
        val updateType = UpdateType.EDIT_PRODUCT

        val actual = productViewModel.validateData(oldProduct, newProduct, updateType)
        val expected = true

        assertEquals(expected, actual)
    }

    @Test
    fun validateData_test_create() {
        val oldProduct = ProductEntity(0, "", "", "", 0.0, "")
        val newProduct = ProductEntity(-1, "name", "description", "", 1.0, "category")
        val updateType = UpdateType.ADD_PRODUCT

        val actual = productViewModel.validateData(oldProduct, newProduct, updateType)
        val expected = true

        assertEquals(expected, actual)
    }

    @Test
    fun updateProduct_test() {
        val expected = true
        val productEntity = ProductEntity(
            1,
            "p1",
            "d1",
            "",
            1.1,
            "c1"
        )
        whenever(productRepository.updateProduct(productEntity))
            .thenReturn(flow {
                emit(true)
            })

        productViewModel.updateProduct(productEntity)

        val responseStatus = productViewModel.updateStatus.getOrAwaitValue()
        assertEquals(expected, responseStatus is ResponseStatus.Success<*>)
    }

    @Test
    fun resetStatus_test() {
        productViewModel.resetStatus()

        val actual = productViewModel.responseStatus.getOrAwaitValue()
        val expected = true

        assertEquals(expected, actual is ResponseStatus.Initial)
    }

    val productList = listOf(
        ProductEntity(1, "Books", "BookDescription", "", 3.5, "Book"),
        ProductEntity(2, "Black Shoes", "Shoes", "", 45.0, "Footwear"),
        ProductEntity(2, "IPhone 14", "Apple Iphone 14", "", 999.99, "Phone")
    )

    @Test
    fun sortList_test_name_asc() {
        val expected = true

        productViewModel.sortList(productList, SortType.NAME_ASC.value)

        val actual = productViewModel.responseStatus.getOrAwaitValue()
        assertEquals(expected, actual is ResponseStatus.Success<*>)
    }

    @Test
    fun sortList_test_name_desc() {
        val expected = true

        productViewModel.sortList(productList, SortType.NAME_DESC.value)

        val actual = productViewModel.responseStatus.getOrAwaitValue()
        assertEquals(expected, actual is ResponseStatus.Success<*>)
    }

    @Test
    fun sortList_test_price_asc() {
        val expected = true

        productViewModel.sortList(productList, SortType.PRICE_ASC.value)

        val actual = productViewModel.responseStatus.getOrAwaitValue()
        assertEquals(expected, actual is ResponseStatus.Success<*>)
    }

    @Test
    fun sortList_test_price_desc() {
        val expected = true

        productViewModel.sortList(productList, SortType.PRICE_DESC.value)

        val actual = productViewModel.responseStatus.getOrAwaitValue()
        assertEquals(expected, actual is ResponseStatus.Success<*>)
    }

    @Test
    fun sortList_test_category() {
        val expected = true

        productViewModel.sortList(productList, SortType.CATEGORY.value)

        val actual = productViewModel.responseStatus.getOrAwaitValue()
        assertEquals(expected, actual is ResponseStatus.Success<*>)
    }


}