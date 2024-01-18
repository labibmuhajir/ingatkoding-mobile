package com.ingatkoding.blog.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "/article") {
        composable("/article") {
            ArticleView(navigateToDetail = {
                navController.navigate("/article/$it")
            })
        }

        composable("/article/{path}") {
            val path = it.arguments?.getString("path") ?: return@composable

            DetailArticleView(path = path, onNavigateUp = { navController.navigateUp() })
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        AppNavigation()
    }
}
