package com.ingatkoding.blog.service

import com.ingatkoding.blog.BuildKonfig
import com.ingatkoding.blog.model.Article
import com.ingatkoding.blog.model.ArticleDetail
import com.ingatkoding.blog.model.Paginated
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface IIngatkodingService {
    suspend fun getArticles(page: Int, limit: Int): Paginated<List<Article>>

    suspend fun getArticle(detailPath: String): ArticleDetail
}

class IngatkodingService(private val httpClient: HttpClient) : IIngatkodingService {
    private val baseUrl = BuildKonfig.BASE_URL

    override suspend fun getArticles(page: Int, limit: Int): Paginated<List<Article>> {
        val path = "v1/article"
        val url = baseUrl + path

        return httpClient.get(url) {
            parameter("page", page)
            parameter("limit", limit)
        }.body<Paginated<List<Article>>>()
    }

    override suspend fun getArticle(detailPath: String): ArticleDetail {
        val path = "v2/article/$detailPath"
        val url = baseUrl + path

        return httpClient.get(url).body<ArticleDetail>()
    }
}