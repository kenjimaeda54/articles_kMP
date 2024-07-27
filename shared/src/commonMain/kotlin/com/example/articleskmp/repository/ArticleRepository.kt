package com.example.articleskmp.repository

import com.example.articleskmp.client.ArticlesClient
import com.example.articleskmp.client.KtorApi
import com.example.articleskmp.client.KtorApiImplementation
import com.example.articleskmp.remote.ArticleRemoteSource
import com.example.articleskmp.util.data.DataOrException
import com.example.articleskmp.util.model.ArticleModel
import com.example.articleskmp.util.model.toArticleModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject



class ArticleRepository: KoinComponent {
    private val articleRemoteSource: ArticleRemoteSource by  inject()



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