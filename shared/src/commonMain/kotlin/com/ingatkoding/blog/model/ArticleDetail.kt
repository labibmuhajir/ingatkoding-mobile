package com.ingatkoding.blog.model

import kotlinx.serialization.Serializable

@Serializable
data class ArticleDetail(
    val createdAt: String,
    val updatedAt: String,
    val id: Long,
    val title: String,
    val content: String,
    val author: Author,
    val tags: List<Tag>,
    val detailPath: String
)
