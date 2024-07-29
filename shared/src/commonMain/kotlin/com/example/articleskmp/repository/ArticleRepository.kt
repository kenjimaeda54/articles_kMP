package com.example.articleskmp.repository

import com.example.articleskmp.client.ArticlesClient
import com.example.articleskmp.client.KtorApi
import com.example.articleskmp.client.KtorApiImplementation
import com.example.articleskmp.local.ArticleLocalSource
import com.example.articleskmp.model.ArticlesResponse
import com.example.articleskmp.remote.ArticleRemoteSource
import com.example.articleskmp.util.data.DataOrException
import com.example.articleskmp.util.model.ArticleModel
import com.example.articleskmp.util.model.toArticleModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject



class ArticleRepository: KoinComponent {
    private val articleRemoteSource: ArticleRemoteSource by inject()
    private val articleLocalSource: ArticleLocalSource by inject()


    //preciso inserir o parmetro na funcao fetchArticles
    //porque preciso garantiur que nao possui erro para apagar o dado local
    // ou atualizar se nao ira gerar excecao na outra ffuncao e acabara estourando na ui
    suspend fun fetchArticles(isForceRefreshing: Boolean): DataOrException<List<ArticleModel>, Exception, Boolean> {

        val articlesDB = articleLocalSource.getArticles()

        //aqui quando o usuario abrir o app sem  preciso verificar se esta sem internete e possui dados para mostrar
        if (articlesDB.isNotEmpty() && !isForceRefreshing) {
            return DataOrException(
                data = articlesDB,
                exception = null,
                isLoading = false
            )
        }

        //se por algum motivo de erro ou seja falta de internet,
        //erro na requisicao preciso garantir em mostrar dados antigos localmente ou caso nao tenha mostrar erro
        //por isso separei
        val articlesResponse = fetchArticlesResponse()

        //se cair algum error preciso verififcar se tem dado para mostrar ou lancar o erro do expection
         if (articlesResponse.exception != null && articlesResponse.data == null ) {
            return DataOrException(
                data = articlesDB.ifEmpty { null },
                exception = if (articlesDB.isNotEmpty()) null  else articlesResponse.exception,
                isLoading = false
            )
        }



        val articles = articlesResponse.data!!.map { it.toArticleModel() }
        if(isForceRefreshing) {
            articleLocalSource.removeAllArticle()
        }
        articleLocalSource.insertAllArticle(articlesResponse.data)
        return DataOrException(
            data = articles,
            exception = null,
            isLoading = false
        )

     }

    private suspend fun fetchArticlesResponse():  DataOrException<List<ArticlesResponse>, Exception, Boolean> {
        return  try {
            val articlesResponse = articleRemoteSource.fetchArticles().articles
            DataOrException(
                data = articlesResponse,
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