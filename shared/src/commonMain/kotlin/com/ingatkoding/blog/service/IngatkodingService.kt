package com.ingatkoding.blog.service

import com.ingatkoding.blog.BuildKonfig
import com.ingatkoding.blog.model.Article
import com.ingatkoding.blog.model.ArticleDetail
import com.ingatkoding.blog.model.Paginated
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface IIngatkodingService {
    suspend fun getArticles(): List<Article>

    suspend fun getArticle(path: String): ArticleDetail
}

class IngatkodingService(private val httpClient: HttpClient) : IIngatkodingService {
    private val baseUrl = BuildKonfig.BASE_URL

    override suspend fun getArticles(): List<Article> {
        val path = "v1/article"
        val url = baseUrl + path
        val response: Paginated<List<Article>> = httpClient.get(url).body()

        return response.data
    }

    override suspend fun getArticle(path: String): ArticleDetail {
        val path = "v2/article/$path"
        val url = baseUrl + path

        return httpClient.get(url).body<ArticleDetail>()
    }
}