package com.ingatkoding.blog.article

import com.ingatkoding.blog.model.Article
import com.ingatkoding.blog.model.ArticleDetail
import com.ingatkoding.blog.model.Author
import com.ingatkoding.blog.model.Paginated
import com.ingatkoding.blog.service.IIngatkodingService
import com.ingatkoding.blog.service.MockIIngatkodingService
import kotlinx.coroutines.runBlocking
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


@UsesMocks(IIngatkodingService::class)
class ArticleRepositoryTest {
    private val mocker = Mocker()
    private lateinit var ingatkodingService: IIngatkodingService
    private lateinit var articleRepository: ArticleRepository

    @BeforeTest
    fun setup() {
        ingatkodingService = MockIIngatkodingService(mocker)
        articleRepository = ArticleRepository(ingatkodingService)
    }

    @AfterTest
    fun tearDown() {
    }

    @Test
    fun testGetArticle() {
        runBlocking {
            val articles = listOf(
                Article(1, "", "", listOf(), Author("", "", ""), "")
            )
            val expected = Paginated(
                1, 1, articles
            )
            mocker.everySuspending {
                ingatkodingService.getArticles(0, 5)
            } returns expected
            val actual = articleRepository.getArticles(0)

            assertEquals(expected, actual)
        }
    }

    @Test
    fun testGetArticleDetail() {
        runBlocking {
            val expected = ArticleDetail("", "", 1, "", "", Author("", "", ""), listOf(), "a")
            val path = expected.detailPath
            mocker.everySuspending {
                ingatkodingService.getArticle(path)
            } returns expected

            val actual = articleRepository.getArticleDetail(path)

            assertEquals(expected, actual)
        }
    }
}