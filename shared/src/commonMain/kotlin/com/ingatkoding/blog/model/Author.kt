package com.ingatkoding.blog.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Author(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("imageUrl")
    val imageUrl: String
)
