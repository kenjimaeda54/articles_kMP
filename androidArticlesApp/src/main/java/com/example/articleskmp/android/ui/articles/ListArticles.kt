package com.example.articleskmp.android.ui.articles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.articleskmp.util.model.Article

@Composable
fun ListArticles(articles: List<Article>) {

    LazyColumn {
        items(articles.size) { index ->
            ArticleItem(article = articles[index])
        }
    }

}


@Composable
fun ArticleItem(article: Article) {

    Column(modifier = Modifier.padding(horizontal = 13.dp, vertical = 20.dp)) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp)),
            model = ImageRequest.Builder(LocalContext.current).data(article.imageUrl)
                .crossfade(true).build(),
            contentDescription = "Image Article", contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = article.title, maxLines = 2, overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = article.description,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
        )
        Text(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .align(alignment = Alignment.End),
            text = article.date, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Light)
        )

    }


}