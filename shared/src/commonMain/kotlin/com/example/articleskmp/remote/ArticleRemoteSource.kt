package com.example.articleskmp.remote

import com.example.articleskmp.client.ArticlesClient

class ArticleRemoteSource(private val articlesClient: ArticlesClient) {
    suspend fun fetchArticles() = articlesClient.fetchArticles()
}