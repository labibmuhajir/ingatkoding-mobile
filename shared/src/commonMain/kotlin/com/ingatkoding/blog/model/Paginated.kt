package com.ingatkoding.blog.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Paginated<T>(
    @SerialName("currentPage")
    val currentPage: Int,
    @SerialName("lastPage")
    val lastPage: Int?,
    @SerialName("data")
    val data: T
)
