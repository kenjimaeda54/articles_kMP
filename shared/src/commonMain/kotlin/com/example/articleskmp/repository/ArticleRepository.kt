package com.example.articleskmp.repository

import com.example.articleskmp.client.ArticlesClient
import com.example.articleskmp.client.KtorApiImplementation
import com.example.articleskmp.remote.ArticleRemoteSource
import com.example.articleskmp.util.data.DataOrException
import com.example.articleskmp.util.model.ArticleModel
import com.example.articleskmp.util.model.toArticleModel

class ArticleRepository {
    private val articleRemoteSource: ArticleRemoteSource

    init {
        val ktorApi = KtorApiImplementation()
        val articlesClient = ArticlesClient(ktorApi)
        articleRemoteSource = ArticleRemoteSource(articlesClient)
    }

    suspend fun fetchArticles(): DataOrException<List<ArticleModel>, Exception, Boolean> {
        return  try {
            val articles = articleRemoteSource.fetchArticles().articles.map {
                it.toArticleModel()
            }
             DataOrException(
                data = articles,
                exception = null,
                isLoading = false
            )
        } catch (e: Exception) {
            DataOrException(
                data = null,
                exception = e,
                isLoading = false
            )
        }
     }

}