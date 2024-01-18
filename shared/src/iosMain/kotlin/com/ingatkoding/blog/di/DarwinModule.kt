package com.ingatkoding.blog.di

import com.ingatkoding.blog.article.ArticleDetailViewModel
import com.ingatkoding.blog.article.ArticleViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val darwinModule = module {
    factoryOf(::ArticleViewModel)
    factoryOf(::ArticleDetailViewModel)
}