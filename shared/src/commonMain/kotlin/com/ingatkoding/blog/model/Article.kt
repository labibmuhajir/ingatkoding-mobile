package com.ingatkoding.blog.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String,
    @SerialName("tags")
    val tags: List<Tag>,
    @SerialName("author")
    val author: Author,
    @SerialName("detailPath")
    val detailPath: String
)
