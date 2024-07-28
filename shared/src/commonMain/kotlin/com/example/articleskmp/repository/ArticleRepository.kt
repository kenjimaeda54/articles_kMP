package com.example.articleskmp.repository

import com.example.articleskmp.client.ArticlesClient
import com.example.articleskmp.client.KtorApi
import com.example.articleskmp.client.KtorApiImplementation
import com.example.articleskmp.local.ArticleLocalSource
import com.example.articleskmp.remote.ArticleRemoteSource
import com.example.articleskmp.util.data.DataOrException
import com.example.articleskmp.util.model.ArticleModel
import com.example.articleskmp.util.model.toArticleModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject



class ArticleRepository: KoinComponent {
    private val articleRemoteSource: ArticleRemoteSource by  inject()
    private  val articleLocalSource: ArticleLocalSource by  inject()


    suspend fun fetchArticles(): DataOrException<List<ArticleModel>, Exception, Boolean> {
        return  try {
            val articlesDB = articleLocalSource.getArticles()
             if(articlesDB.isNotEmpty()) {
                return DataOrException(
                    data = articlesDB,
                    exception = null,
                    isLoading = false
                )
            }
            val articlesResponse = articleRemoteSource.fetchArticles().articles
            val articles = articlesResponse.map { it.toArticleModel() }
            articleLocalSource.insertAllArticle(articlesResponse)
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