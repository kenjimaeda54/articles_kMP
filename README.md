# Articles
Projeto para consultar as princiapis noticias do Brasil sobre negocios

## Features
- Novamente usei buildConfig para quardar minhas chaves de API, em Multi Platafforma muda bem pouco do padrao
- Para funcionar o projeto precisa adcionar sua [api_Key](https://newsapi.org/)
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
