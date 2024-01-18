package com.ingatkoding.blog.di

import com.ingatkoding.blog.article.ArticleRepository
import com.ingatkoding.blog.article.IArticleRepository
import com.ingatkoding.blog.service.IIngatkodingService
import com.ingatkoding.blog.service.IngatkodingService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
    singleOf(::IngatkodingService) { bind<IIngatkodingService>() }
    singleOf(::ArticleRepository) { bind<IArticleRepository>() }
}