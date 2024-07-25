package com.example.articleskmp.util.viewmodel

import com.example.articleskmp.util.client.HttpClient
import com.example.articleskmp.util.BaseViewModel
import com.example.articleskmp.util.data.DataOrException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.articleskmp.util.model.Article

class ArticlesViewModel: BaseViewModel(), HttpClient {
    private val _articles = MutableStateFlow<DataOrException<List<Article>, Exception, Boolean>>(
        DataOrException(
        data = null,
        exception = null,
        isLoading = true
    )
    )
    val articles: StateFlow<DataOrException<List<Article>, Exception, Boolean>> get()  = _articles

    //abaixo uma gambiarra ate criar a camada de servvico
    override suspend fun fetchArticles(): List<Article> {
       return listOf(
           Article(
               title = "Messi reage à confusão em Argentina x Marrocos na Olimpíada; veja\n",
               description = "Principal astro da Seleção Argentina principal, Lionel Messi foi às redes sociais se expressar sobre a confusão no duelo dos argentinos com o Marrocos, na estreia da equipe olímpica da Albiceleste em Paris 2024.\n" +
                       "\n",
               imageUrl = "https://www.cnnbrasil.com.br/wp-content/uploads/sites/12/2024/06/messi-argentina-guatemala-e1718811255101.jpg?w=1220&h=674&crop=1&quality=50",
               date = "12/10/2024"
            ),
           Article(
               title = "Cópia de um projeto de pesquisa da Universidade de São Paulo\n",
               description = "O projeto de pesquisa da Universidade de São Paulo (USP) em 2019, que investigava o uso de GPS para identificação de animais de estágio e identificação de animais de estágio em laborato",
               date = "12/10/2024",
               imageUrl = "https://www.cnnbrasil.com.br/wp-content/uploads/sites/12/2024/06/messi-argentina-guatemala-e1718811255101.jpg?w=1220&h=674&crop=1&quality=50"
           ),
           Article(
               title= "O Brasil tem 2.000.000 de alunos no Ensino Fundamental\n",
               description = "O Brasil tem 2.000.000 de alunos no Ensino Fundamental. O número de alunos no Ensino Fundamental no Brasil tem aumentado de 1.000.000 para 2.000.000",
               date = "12/10/2024",
               imageUrl = "https://www.cnnbrasil.com.br/wp-content/uploads/sites/12/2024/06/messi-argentina-guatemala-e1718811255101.jpg?w=1220&h=674&crop=1&quality=50"
           )


       )
    }

    fun getArticles() {
        scope.launch {
            _articles.value = DataOrException(
                data = fetchArticles(),
                exception = null,
                isLoading = false
            )
        }
     }



}