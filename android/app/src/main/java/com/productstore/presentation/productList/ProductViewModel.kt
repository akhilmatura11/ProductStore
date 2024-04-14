package com.productstore.presentation.productList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productstore.data.localDataSource.ProductEntity
import com.productstore.domain.ProductRepository
import com.productstore.domain.ResponseStatus
import com.productstore.domain.SortType
import com.productstore.domain.UpdateType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private var _responseStatus = MutableLiveData<ResponseStatus>(ResponseStatus.Initial)
    val responseStatus: LiveData<ResponseStatus> = _responseStatus

    private var _updateStatus = MutableLiveData<ResponseStatus>(ResponseStatus.Initial)
    val updateStatus: LiveData<ResponseStatus> = _updateStatus

    private var sortAsc = SortType.NAME_ASC

    fun fetchProducts() {
        _responseStatus.value = ResponseStatus.Initial
        sortAsc = SortType.NAME_ASC
        viewModelScope.launch {
            _responseStatus.value = ResponseStatus.Loading
            productRepository.fetchProductLists()
                .catch {
                    _responseStatus.value = ResponseStatus.Error(it.message.orEmpty())
                }
                .collect { list ->
                    _responseStatus.value = ResponseStatus.Success(list.sortedBy { it.name })
                }
        }
    }

    fun validateData(
        oldProduct: ProductEntity,
        newProduct: ProductEntity,
        updateType: UpdateType
    ): Boolean {
        return if (updateType == UpdateType.ADD_PRODUCT) {
            newProduct.name.isNotBlank()
                    && newProduct.description.isNotBlank()
                    && newProduct.category.isNotBlank()
                    && newProduct.price >= 0.0
        } else {
            oldProduct.name != newProduct.name
                    || oldProduct.description != newProduct.description
                    || oldProduct.category != newProduct.category
                    || oldProduct.image != newProduct.image
                    || oldProduct.price != newProduct.price
        }
    }

    fun updateProduct(newProduct: ProductEntity) {
        _updateStatus.value = ResponseStatus.Initial
        viewModelScope.launch {
            _updateStatus.value = ResponseStatus.Loading
            productRepository.updateProduct(newProduct)
                .catch {
                    _updateStatus.value = ResponseStatus.Error(it.message.orEmpty())
                }
                .collect {
                    _updateStatus.value = ResponseStatus.Success(it)
                }
        }


    }

    fun resetStatus() {
        _updateStatus.value = ResponseStatus.Initial
        _responseStatus.value = ResponseStatus.Initial
    }

    fun getBytes(iStream: InputStream?): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        iStream?.let { inputStream ->
            val bufferSize = 1024 * 100
            val buffer = ByteArray(bufferSize)

            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
        }
        return byteBuffer.toByteArray()
    }

    fun sortList(productList: List<ProductEntity>, sortType: String) {
        _responseStatus.value = ResponseStatus.Success(
            when (sortType) {
                SortType.NAME_ASC.value -> productList.sortedBy { it.name }
                SortType.NAME_DESC.value -> productList.sortedByDescending { it.name }
                SortType.PRICE_ASC.value -> productList.sortedBy { it.price }
                SortType.PRICE_DESC.value -> productList.sortedByDescending { it.price }
                SortType.CATEGORY.value -> productList.sortedBy { it.category }
                else -> {

                }
            }
        )
    }
}
