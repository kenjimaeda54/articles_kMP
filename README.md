# Articles
Projeto para consultar as principais notícias do Brasil sobre negócios


## Motivacao
- Aprender uso do KMP , utilizar MVVM e offline first

## Features
- Novamente usei buildConfig para guardar minhas chaves de API, em cross plataforma muda bem pouco do padrão.
- Para funcionar, o projeto precisa obter sua  [api_Key](https://newsapi.org/) no site.
- Precisa adicionar a variável no arquivo local.properties, [este artigo](https://stackoverflow.com/questions/20673378/where-does-local-properties-go-for-android-project) consegue localizar onde está o arquivo.


```kotlin
//instalar o plugin
id("com.github.gmazzo.buildconfig") version "5.44.0"


val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    val apiKey = properties.getProperty("API_KEY")
    buildConfig {
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
    }

// usar onde deseja
private  val apikey = BuildConfig.API_KEY

```

##
- A arquitetura usada na camada de negócio foi MVVM, abaixo um exemplo como implementar.
- IOS não possui a ViewModel do kotlin,  para isso usei touchlab para traduzir.
- KMP  usa o commonMain mais os arquivos androidMain e  iosMain para compartilhar as regras de negócio entre as plataformas.
- No caso do ViewModel precisei usar os arquivos específicos para cada plataforma, motivo qual a implementação muda no IOS
- Repara que no projeto preciso construir as viewModel nos respectivos diretórios e também no commonModuel
- KMP utiliza um conceito de expect e actual para se comunicar com as respectivas plataformas.
- Repara que IOS não possui o viewModel por isso criamos uma implementação própria.

```Kotlin



//build.gradle
//instalar dependencia 


//plugin
id("co.touchlab.skie") version "0.8.3"

//commonMain

commonMain.dependencies{
  implementation(libs.coroutines.ktx)
  implementation(libs.touchlab.stately.common)
}


//androidMain
androidMain.dependencies {
  implementation(libs.viewModel.ktx)


}

//iosMain
iosMain.dependencies {
  implementation(libs.touchlab.stately.isolate.collections)
  implementation(libs.touchlab.stately.isolate)
}

//shared:   util/BaseViewModel

//open e para permitir alterar a classe, idêntico à herança.

expect  open class BaseViewModel() {

    val scope: CoroutineScope

}


//androidMain: util/BaseViewModel

actual  open class BaseViewModel: ViewModel() {

    actual val scope: CoroutineScope = viewModelScope
}


//iosMain: util/BaseViewModel
actual  open class BaseViewModel {

    actual val scope = CoroutineScope(Dispatchers.IO)

    fun clear() {
        scope.cancel()
    }
}


//para utlizar

//commmonMain
//crio o viewModel
class ArticlesViewModel: BaseViewModel(),KoinComponent {
    private val _articles = MutableStateFlow<DataOrException<List<ArticleModel>, Exception, Boolean>>(
        DataOrException(
        data = null,
        exception = null,
        isLoading = true
    )
    )
    val articles: StateFlow<DataOrException<List<ArticleModel>, Exception, Boolean>> get()  = _articles
    private val articleRepository: ArticleRepository by  inject()

    init {
        getArticles()
    }

    fun getArticles(isForcingRefreshing: Boolean = false) {
        scope.launch {
            _articles.value = articleRepository.fetchArticles(isForcingRefreshing)
         }
     }



}


//consumo nas UI com as respectivas plataformas
// no android basta usar como fazemos no compose

val articlesViewModel = ArticlesViewModel()
val articles by articlesViewModel.articles.collectAsState()


//no IOS precisamos fazer um warp
//preciso do MainActor se não ira acusar erro que o @Published não pode ser atualizado no thread principal.
//preciso do skie para comunicar

@MainActor
class ArticlesState: ObservableObject {

    //toda vez que o @Published alterar afeta a UI
	@Published var loading = LoadingState.loading
	let viewModel: ArticlesViewModel = ArticlesViewModel()
	var articlesModel: [ArticleModel] = []
	
	
  	func fetchArticles() async  {
		loading = .loading //para redesenhar a UI
		for await viewModel in viewModel.articles {
			
			if(viewModel.exception != nil){
				loading = .failure
			}
			
			if let data = viewModel.data as? [ArticleModel] {
				self.loading = .sucess
				self.articlesModel = data
			}
			
			
		}
		
	}
	
	func refreshDataArticles() {
		viewModel.getArticles(isForcingRefreshing: true)
		
	}
	

	
	deinit {
		viewModel.clear()
	}
	
}

```


##

- Para lidar com requisições, usei o ktor abaixo como implementar.
- Usando o conceito de MVVM e implementado offline, usei uma camada para decidir qual dado deveria ser apresentado.
- A implementação está no diretorio repository

```kotlin

//build.gradle

//plugin para serialization
kotlin("plugin.serialization") version("1.9.20")


//
commonMain.dependencies {

 implementation(libs.ktor.client.core)
 implementation(libs.ktor.serialization.json)
 implementation(libs.ktor.client.content.negotiation)


}

iosMain.dependencies {
 implementation(libs.ktor.client.darwin)


}

androidMain.dependencies {
 implementation(libs.ktor.client.android)

}


//model


//Entity

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


//ArticleModel
data class ArticleModel (
    val id:   String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val date: String
)

//preciso transformar para model, apos serializar
fun ArticlesResponse.toArticleModel() = ArticleModel(
    id =  "${(nextDouble() * 13224343 + nextFloat() + nextDouble() * 3403403403 ) } $title $publishedAt",
    title = title,
    description = description ?: "Without description",
    imageUrl = urlToImage ?: "https://media.istockphoto.com/id/1147544807/pt/vetorial/thumbnail-image-vector-graphic.webp?s=1024x1024&w=is&k=20&c=UdpQxmFWK0gkGWWYztnDThtFsBZik9eDGrxyQ4rYCEs=" ,
    date = formatDate(publishedAt)
)

//aqui e para converter o ArticleModel que esta localmente salvo
fun ArticlesData.toArticleModel() =  ArticleModel(
    id =  "${(nextDouble() * 13224343 + nextFloat() + nextDouble() * 3403403403 ) } $title $publishedAt",
    title = title,
    description = description ?: "Without description",
    imageUrl = urlToImage ?: "https://media.istockphoto.com/id/1147544807/pt/vetorial/thumbnail-image-vector-graphic.webp?s=1024x1024&w=is&k=20&c=UdpQxmFWK0gkGWWYztnDThtFsBZik9eDGrxyQ4rYCEs=" ,
    date = formatDate(publishedAt)
)



//para implementar o ktor criei dentro do diretorio client

//KtorApi
interface KtorApi {
    val client: HttpClient
    fun HttpRequestBuilder.apiUrl(path: String)
    fun HttpRequestBuilder.json()

}

//ArticlesClient
class ArticlesClient(private val ktorApi: KtorApi) : KtorApi by ktorApi {
   private  val apikey = BuildConfig.API_KEY


    suspend fun fetchArticles(): ArticleEntity {
        return client.get {
            apiUrl("/v2/top-headlines?country=us&category=business&apiKey=$apikey")
        }.body()
    }

}



//KtorApiImplementation
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



```

## 
- Para injeção dependência usei o koin
- Para banco local usei o sqlDeligth
- No android utilizei a função initKoin, esta função está no shared, ela carrega todos os módulos do koin


```kotlin

commonMain.dependencies {
 implementation(libs.koin.core)

}

androidMain.dependencies {
   implementation(libs.koin.android)
}


//para iniciar o koin pelo android

 initKoin {
            androidContext(this@MainApplication)
}

//shared: di/CommonModule

//diferença entre single e factory, que com a palavra reservada factory sempre inicia uma nova instancia

fun initKoin(appDeclaration: KoinAppDeclaration) = startKoin {
    appDeclaration()
    modules(
        clientModule,
        viewModelModule,
        repositoryModule,
        driverSQLModule,
        coreDatabase
    )
}

private val clientModule = module {
     factory {ArticlesClient(get())} //factory sempre seraa uma novaa instancia da classe ArticlesClient
     single<KtorApi> { KtorApiImplementation() } //single   vai retornar a mesma instancia do KtorApiImplementation, sempre que for chamado> { }
}

private val viewModelModule = module {
   single { ArticlesViewModel() }
}

private val repositoryModule = module {
    single { ArticleRemoteSource(get()) }
    single { ArticleRepository() }
    single { ArticleLocalSource(get()) }
}

private val coreDatabase = module {
    single { ArticlesDB(get()) }
}

fun initKoin() = initKoin {  } 

//no ios simplesmente pego o nome do arquivo,proprie skie disponibiliza

@main
struct iOSApp: App {
         //nome do arquivo no shared CommonModule
        //kt e inserido automatico
	init() {
          CommonModuleKt.doInitKoin()
	}

	var body: some Scene {
		WindowGroup {
			ArticlesScreen()
		}
	}
}

// para iniciar a classe que esta no module do Koin usamos a palavra reservada inject

private val articleRemoteSource: ArticleRemoteSource by inject()
```


## 

- Para SqlDeLigth as querys ficam na mesma raiz do shared, se não expandir os arquivos, a estrutura deveria ser mais ou menos assim: seupacote/kotlin , seupacote/sqldeligth.
- Para aparecer o arquivo, pode usar uma extensão do Android Studio(SqlDeLigth), mas quando for editar o arquivo precisa remover ele, pois não está funcionando na nova versão Koala

```kotlin

//build.gralde
//plugin sqldelight
alias(libs.plugins.sqldelight)


commonMain.dependencies {
 implementation(libs.sql.coroutines.extensions)

}


iosMain.dependencies {
implementation(libs.sql.native.driver)

}

androidMain.dependencies {
implementation(libs.sql.android.driver)

}



//no arquivo build.gradle
sqldelight {
    databases {
        create("ArticlesDB") {
            packageName = "com.example.articleskmp.db" //onde ira escrever as querys
         }
     }
}


//usando koin criei os drivers para cada plataforma

//iosMain
actual  val  driverSQLModule  = module {
    single<SqlDriver> {
        NativeSqliteDriver(ArticlesDB.Schema, "ArticlesDB.db")
    }
}


//androidMain
actual  val  driverSQLModule  = module {

    single<SqlDriver> {
        AndroidSqliteDriver(ArticlesDB.Schema, get(), "ArticlesDB.db")
    }

}
```


