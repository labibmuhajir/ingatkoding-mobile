package com.ingatkoding.blog.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.navigation.compose.rememberNavController
import com.ingatkoding.blog.article.ArticleDetailViewModel
import com.ingatkoding.blog.model.ArticleDetail
import com.ingatkoding.blog.model.CommonState
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailArticleView(
    path: String,
    articleDetailViewModel: ArticleDetailViewModel = getViewModel(),
    onNavigateUp: () -> Unit
) {
    LaunchedEffect(Unit) {
        articleDetailViewModel.getArticle(path)
    }
    val state = articleDetailViewModel.articleState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Article Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Icon Back")
                    }
                })
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (state.value) {
                is CommonState.Error -> Text(text = (state.value as CommonState.Error).message)
                CommonState.Initial -> {}
                CommonState.Loading -> CircularProgressIndicator()
                CommonState.NoData -> {}
                is CommonState.Success -> {
                    val detail = (state.value as CommonState.Success<ArticleDetail>).data

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = detail.title, style = MaterialTheme.typography.titleMedium)

                        AuthorView(author = detail.author)

                        Spacer(modifier = Modifier.height(8.dp))

                        val render = HtmlCompat.fromHtml(
                            detail.content,
                            HtmlCompat.FROM_HTML_MODE_COMPACT
                        )
                        Text(text = buildAnnotatedString { append(render) })
                    }
                }
            }
        }
    }
}