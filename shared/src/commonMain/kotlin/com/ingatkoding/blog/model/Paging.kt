package com.ingatkoding.blog.model

data class Paging<T>(
    val currentData: List<T>,
    val nextState: NextState
)