package com.example.articleskmp.util.model

import com.example.articleskmp.model.ArticlesResponse
import com.example.articleskmp.util.formatDate
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextFloat


data class ArticleModel (
    val id:   Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val date: String
)


fun ArticlesResponse.toArticleModel() = ArticleModel(
    id =  (nextDouble() * 13224343 + nextFloat() + nextDouble() * 3403403403 ).toInt(),
    title = title,
    description = description ?: "Without description",
    imageUrl = urlToImage ?: "https://media.istockphoto.com/id/1147544807/pt/vetorial/thumbnail-image-vector-graphic.webp?s=1024x1024&w=is&k=20&c=UdpQxmFWK0gkGWWYztnDThtFsBZik9eDGrxyQ4rYCEs=" ,
    date = formatDate(publishedAt)
)

