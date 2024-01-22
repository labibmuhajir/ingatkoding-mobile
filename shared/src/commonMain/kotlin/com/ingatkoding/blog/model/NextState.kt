package com.ingatkoding.blog.model

sealed interface NextState {
    data object Loading : NextState
    data class Error(val message: String, val retry: () -> Unit) : NextState
    data object EndPage : NextState
}