package com.ingatkoding.blog.model

sealed interface CommonState<out T> {
    data object Initial : CommonState<Nothing>
    data object Loading : CommonState<Nothing>
    data object NoData : CommonState<Nothing>
    data class Success<T>(val data: T) : CommonState<T>
    data class Error(val message: String, val retry: suspend () -> Unit) : CommonState<Nothing>
}