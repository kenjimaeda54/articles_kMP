package com.example.articleskmp.util.viewmodel

import com.example.articleskmp.repository.ArticleRepository
import com.example.articleskmp.util.BaseViewModel
import com.example.articleskmp.util.data.DataOrException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.articleskmp.util.model.ArticleModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class ArticlesViewModel: BaseViewModel() {
    private val _articles = MutableStateFlow<DataOrException<List<ArticleModel>, Exception, Boolean>>(
        DataOrException(
        data = null,
        exception = null,
        isLoading = true
    )
    )
    val articles: StateFlow<DataOrException<List<ArticleModel>, Exception, Boolean>> get()  = _articles
    private  var articleRepository: ArticleRepository = ArticleRepository()

    init {
        getArticles()
    }



    private fun getArticles() {
        scope.launch {
            _articles.value = articleRepository.fetchArticles()
         }
     }

     fun observeArticles(onchange: (DataOrException<List<ArticleModel>, Exception, Boolean>) -> Unit) {
          articles.onEach {
              onchange(it)
          }.launchIn(scope)
     }

}