package com.example.articleskmp.client

  import io.ktor.client.HttpClient
  import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
  import io.ktor.client.request.HttpRequestBuilder
  import io.ktor.http.ContentType
  import io.ktor.http.contentType
  import io.ktor.http.encodedPath
  import io.ktor.http.takeFrom
  import io.ktor.serialization.kotlinx.json.json
  import kotlinx.serialization.json.Json

class KtorApiImplementation() : KtorApi {

    private val apiURl = "https://newsapi.org"


    override  val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }
    }

    override fun HttpRequestBuilder.apiUrl(path: String) {
        url {
            takeFrom(apiURl)
            encodedPath = path
        }
     }

    override fun HttpRequestBuilder.json() {
        contentType(ContentType.Application.Json)
     }


}