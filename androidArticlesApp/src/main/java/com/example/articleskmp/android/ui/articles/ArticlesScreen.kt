package com.example.articleskmp.android.ui.articles

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
  import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.articleskmp.util.viewmodel.ArticlesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun ArticlesScreen() {
    val articlesViewModel = ArticlesViewModel()
    val articles by articlesViewModel.articles.collectAsState()




    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                text = "Artigos",
                modifier = Modifier
                    .padding(10.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
            )
           if (articles.data?.isEmpty() == true) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Sem artigos")
                }

            } else if (articles.exception != null) {


                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Problema ao carregar os artigos")
                }

            } else {
               SwipeRefresh(
                   state = rememberSwipeRefreshState(isRefreshing = articles.isLoading),
                   onRefresh = {
                       articlesViewModel.getArticles(true)
                   }) {
                   articles.data?.let { ListArticles(articleModels = it) }
               }
           }
        }


    }
}