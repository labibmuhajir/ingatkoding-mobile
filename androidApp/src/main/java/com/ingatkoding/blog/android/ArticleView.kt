package com.ingatkoding.blog.android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.ingatkoding.blog.article.ArticleViewModel
import com.ingatkoding.blog.model.Article
import com.ingatkoding.blog.model.CommonState
import com.ingatkoding.blog.model.NextState
import com.ingatkoding.blog.model.Paging
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleView(
    navigateToDetail: (path: String) -> Unit,
    articleViewModel: ArticleViewModel = getViewModel()
) {
    LaunchedEffect(Unit) {
        articleViewModel.getArticles()
    }

    val scrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Article") })
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            val state = articleViewModel.articleState.collectAsState()

            when (state.value) {
                is CommonState.Error -> {
                    val errorState = (state.value as CommonState.Error)

                    ErrorView(message = errorState.message, retry = errorState.retry)
                }

                CommonState.Initial -> {}
                CommonState.Loading -> CircularProgressIndicator()
                CommonState.NoData -> Text("No Data")
                is CommonState.Success -> {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {

                        LazyColumn(
                            state = scrollState
                        ) {
                            val dataState = (state.value as? CommonState.Success<Paging<Article>>)
                                ?: return@LazyColumn

                            items(dataState.data.currentData) {
                                ArticleItem(article = it, modifier = Modifier.clickable {
                                    navigateToDetail(it.detailPath)
                                })
                            }

                            item {
                                when (dataState.data.nextState) {
                                    NextState.EndPage -> {}
                                    is NextState.Error -> {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            val errorState =
                                                (dataState.data.nextState as NextState.Error)

                                            ErrorView(
                                                message = errorState.message,
                                                retry = errorState.retry
                                            )
                                        }
                                    }

                                    NextState.Loading -> {
                                        val isLastItem =
                                            scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == scrollState.layoutInfo.totalItemsCount - 1

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                        LaunchedEffect(isLastItem) {
                                            if (isLastItem) articleViewModel.getNextArticle()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleItem(article: Article, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = article.title, style = MaterialTheme.typography.titleLarge)

        AuthorView(author = article.author)

        Spacer(modifier = Modifier.height(8.dp))

        val render = HtmlCompat.fromHtml(
            article.content,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        Text(text = buildAnnotatedString { append(render) })

        Divider()

        Spacer(modifier = Modifier.height(12.dp))

    }
}