package com.example.articleskmp.client

import Articles_KMP.shared.BuildConfig
import com.example.articleskmp.model.ArticleEntity
import io.ktor.client.call.body
import io.ktor.client.request.get

class ArticlesClient(private val ktorApi: KtorApi) : KtorApi by ktorApi {
   private  val apikey = BuildConfig.API_KEY


    suspend fun fetchArticles(): ArticleEntity {
        return client.get {
            apiUrl("/v2/top-headlines?country=us&category=business&apiKey=$apikey")
        }.body()
    }

}