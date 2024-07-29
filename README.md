# Articles
Projeto para consultar as princiapis noticias do Brasil sobre negocios

## Features
- Novamente usei buildConfig para quardar minhas chaves de API, em Multi Platafforma muda bem pouco do padrao
- Para funcionar o projeto precisa adcionar sua [api_Key](https://newsapi.org/)
- Precisa adicionar a variavel no arquivo loca.properties, [este artigo](https://stackoverflow.com/questions/20673378/where-does-local-properties-go-for-android-project)  consegue saber onde esta o arquivo


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
- Arquitetura usada na camada logica foi MVVM, abaixo um exemplo como implementar
- IOS precisa implementar a ViewModel do kotlin para isso usei touchlab para traduzir as ViewModel
- KMP  usa o commonMain mais os arquivos androidMain é iosMain para compartilhar as regras de negocio entre as plataformas
- No caso do ViewModel precisei usar os arquivos especificos para cada plataforma,motivo que a implementacao muda no IOS
- Repara que no projeto preciso construir as viewModel nos respectivos caminhos e tambem mesmo nome de arquivo
- KMP utiliza um conceito de expect e actual para se comunicar com as respectivas plataformmas
- Repara que IOS nao possui o viewModel por isso criammos uma implementacao propria

```Kotlin



//build.gradle
//instalar dependencia 


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

//open e para permitir alterar a classe, identico a hernca

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
//preciso do MainActor se nao ira acusar erro que o @Published nao pode ser sofrer update no tread principal
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
