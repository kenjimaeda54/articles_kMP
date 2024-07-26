package com.example.articleskmp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleEntity (
     val status: String,
     val totalResults: Int,
     val articles: List<ArticlesResponse>
)

@Serializable
data class ArticlesResponse (
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String
)