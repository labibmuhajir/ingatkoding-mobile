package com.ingatkoding.blog.article

import com.ingatkoding.blog.BaseViewModel
import com.ingatkoding.blog.model.Article
import com.ingatkoding.blog.model.CommonState
import com.ingatkoding.blog.model.NextState
import com.ingatkoding.blog.model.Paging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ArticleViewModel(
    private val articleRepository: IArticleRepository
) : BaseViewModel() {
    private val _articleState: MutableStateFlow<CommonState<Paging<Article>>> =
        MutableStateFlow(CommonState.Initial)
    val articleState: StateFlow<CommonState<Paging<Article>>> get() = _articleState

    private var currentPage: Int = 1
    private var curretArticles: MutableList<Article> = mutableListOf()

    fun getArticles() {
        scope.launch(Dispatchers.Default) {
            try {
                if (articleState.value is CommonState.Loading) return@launch

                _articleState.emit(CommonState.Loading)

                currentPage = 1

                val response = articleRepository.getArticles(currentPage)
                val articles = sliceArticle(response.data)

                val result = if (articles.isEmpty()) CommonState.NoData else {
                    curretArticles.addAll(articles)
                    val nextState =
                        if (response.currentPage == response.lastPage) NextState.EndPage else NextState.Loading

                    CommonState.Success(
                        Paging(
                            curretArticles.toList(),
                            nextState
                        )
                    )
                }
                _articleState.emit(result)
            } catch (e: Exception) {
                _articleState.emit(CommonState.Error(e.message ?: "Error") {
                    getArticles()
                })
            }
        }
    }

    fun getNextArticle() {
        scope.launch(Dispatchers.Default) {
            try {
                val response = articleRepository.getArticles(currentPage + 1)
                val articles = sliceArticle(response.data)

                if (currentPage != response.currentPage && articles.isNotEmpty()) {
                    curretArticles.addAll(articles)
                    currentPage = response.currentPage
                }

                val isLastPage = response.currentPage == response.lastPage
                val nextState = if (articles.isEmpty() || isLastPage) NextState.EndPage else NextState.Loading
                val result = CommonState.Success(
                    Paging(
                        curretArticles.toList(),
                        nextState,
                    )
                )

                _articleState.emit(result)
            } catch (e: Exception) {
                _articleState.emit(
                    CommonState.Success(
                        Paging(
                            curretArticles.toList(),
                            NextState.Error(e.message ?: "Error") {
                                getNextArticle()
                            }
                        )
                    )
                )
            }
        }
    }

    private suspend fun sliceArticle(articles: List<Article>) =
        supervisorScope {
            articles.map {
                async {
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
            }.awaitAll()
        }

}