package com.example.articleskmp.util.client

import com.example.articleskmp.util.model.Article

interface HttpClient {
    suspend fun fetchArticles(): List<Article>
}