package com.ingatkoding.blog.di

import com.ingatkoding.blog.article.ArticleDetailViewModel
import com.ingatkoding.blog.article.ArticleViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun startKoin() {
    org.koin.core.context.startKoin {
        modules(commonModule, darwinModule)
    }
}

class ArticleViewInjector : KoinComponent {
    val articleViewModel: ArticleViewModel by inject()
}

class ArticleDetailViewInjector : KoinComponent {
    val articleDetailViewModel: ArticleDetailViewModel by inject()
}