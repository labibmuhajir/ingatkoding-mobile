package com.ingatkoding.blog.article

import com.ingatkoding.blog.BaseViewModel
import com.ingatkoding.blog.model.ArticleDetail
import com.ingatkoding.blog.model.CommonState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticleDetailViewModel(private val articleRepository: IArticleRepository) : BaseViewModel() {
    private val _articleState: MutableStateFlow<CommonState<ArticleDetail>> =
        MutableStateFlow(CommonState.Initial)
    val articleState: StateFlow<CommonState<ArticleDetail>> get() = _articleState

    fun getArticle(path: String) {
        scope.launch(Dispatchers.Default) {
            try {
                if (articleState.value is CommonState.Loading) return@launch

                _articleState.emit(CommonState.Loading)

                val articleDetail = articleRepository.getArticleDetail(path)

                _articleState.emit(CommonState.Success(articleDetail))
            } catch (e: Exception) {
                _articleState.emit(CommonState.Error(e.message ?: "Error") {
                    getArticle(path)
                })
            }
        }
    }
}