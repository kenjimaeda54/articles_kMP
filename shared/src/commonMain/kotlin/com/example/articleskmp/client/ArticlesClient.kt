package com.example.articleskmp.client

import com.example.articleskmp.model.ArticleEntity
import io.ktor.client.call.body
import io.ktor.client.request.get

class ArticlesClient(private val ktorApi: KtorApi) : KtorApi by ktorApi {

    suspend fun fetchArticles(): ArticleEntity {
        return client.get {
            apiUrl("/v2/top-headlines?country=us&category=business&apiKey=e03da12b408445449464ceb16db4963a")
        }.body()
    }

}