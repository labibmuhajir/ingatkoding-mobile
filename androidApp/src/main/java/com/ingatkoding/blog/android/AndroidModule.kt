package com.ingatkoding.blog.android

import com.ingatkoding.blog.article.ArticleDetailViewModel
import com.ingatkoding.blog.article.ArticleViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val androidModule = module {
    viewModelOf(::ArticleViewModel)
    viewModelOf(::ArticleDetailViewModel)
}