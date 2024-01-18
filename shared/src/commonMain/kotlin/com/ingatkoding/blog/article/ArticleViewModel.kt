package com.ingatkoding.blog.article

import com.ingatkoding.blog.BaseViewModel
import com.ingatkoding.blog.model.Article
import com.ingatkoding.blog.model.CommonState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val articleRepository: IArticleRepository
) : BaseViewModel() {
    private val _articleState: MutableStateFlow<CommonState<List<Article>>> =
        MutableStateFlow(CommonState.Initial)
    val articleState: StateFlow<CommonState<List<Article>>> get() = _articleState

    fun getArticles() {
        scope.launch(Dispatchers.Default) {
            try {
                if (articleState.value is CommonState.Loading) return@launch

                _articleState.emit(CommonState.Loading)
                val articles = articleRepository.getArticles().map {
                    val end = 300
                    val countContent = it.content.count()
                    val max = if (countContent < 300) countContent - 1 else end

                    Article(
                        it.id,
                        it.title,
                        it.content.slice(0..max),
                        it.tags,
                        it.author,
                        it.detailPath
                    )
                }

                val result = if (articles.isEmpty()) CommonState.NoData else CommonState.Success(articles)
                _articleState.emit(result)
            } catch (e: Exception) {
                _articleState.emit(CommonState.Error(e.message ?: "Error") {
                    getArticles()
                })
            }
        }
    }
}