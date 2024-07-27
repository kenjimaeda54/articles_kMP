package com.example.articleskmp.di

import com.example.articleskmp.client.ArticlesClient
import com.example.articleskmp.client.KtorApi
import com.example.articleskmp.client.KtorApiImplementation
import com.example.articleskmp.db.ArticlesDB
import com.example.articleskmp.local.ArticleLocalSource
import com.example.articleskmp.remote.ArticleRemoteSource
import com.example.articleskmp.repository.ArticleRepository
import com.example.articleskmp.util.viewmodel.ArticlesViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


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