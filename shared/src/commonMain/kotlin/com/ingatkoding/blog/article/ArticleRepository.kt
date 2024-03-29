package com.ingatkoding.blog.article

import com.ingatkoding.blog.model.Article
import com.ingatkoding.blog.model.ArticleDetail
import com.ingatkoding.blog.model.Paginated
import com.ingatkoding.blog.service.IIngatkodingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

interface IArticleRepository {
    suspend fun getArticles(page: Int): Paginated<List<Article>>
    suspend fun getArticleDetail(path: String): ArticleDetail
}

class ArticleRepository(private val ingatkodingService: IIngatkodingService) : IArticleRepository {
    override suspend fun getArticles(page: Int): Paginated<List<Article>> = withContext(Dispatchers.IO) {
        ingatkodingService.getArticles(page, 5)
    }

    override suspend fun getArticleDetail(path: String): ArticleDetail =
        withContext(Dispatchers.IO) {
            ingatkodingService.getArticle(path)
        }
}