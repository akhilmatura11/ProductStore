package com.productstore.domain

sealed class ResponseStatus {
    data object Initial : ResponseStatus()
    data object Loading : ResponseStatus()
    data class Success<T>(val data: T) : ResponseStatus()
    data class Error(val message: String) : ResponseStatus()
}
