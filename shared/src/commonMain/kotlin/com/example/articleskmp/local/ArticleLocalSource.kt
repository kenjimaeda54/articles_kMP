package com.example.articleskmp.local

import app.cash.sqldelight.db.SqlDriver
import com.example.articleskmp.db.ArticlesDB
import com.example.articleskmp.db.ArticlesDBQueryQueries
import com.example.articleskmp.model.ArticlesResponse
import com.example.articleskmp.util.model.ArticleModel
import com.example.articleskmp.util.model.toArticleModel

class ArticleLocalSource(private val database: ArticlesDB)  {


      fun getArticles(): List<ArticleModel> {
      return database.articlesDBQueryQueries.getAllArticle().executeAsList().map {
          it.toArticleModel()
      }
    }


    fun removeAllArticle() {
        database.articlesDBQueryQueries.removeAllArticle()
    }

    fun insertAllArticle(articles: List<ArticlesResponse>) {
        database.transaction {
            articles.forEach {
                insertOnlyArticle(it)
            }
        }
    }
    private fun insertOnlyArticle(article: ArticlesResponse) {
        database.articlesDBQueryQueries.insertArticle(
            article.title,
            article.description,
            article.urlToImage,
            article.publishedAt
        )
    }

}