package com.example.articleskmp.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.articleskmp.db.ArticlesDB
import org.koin.dsl.module

actual  val  driverSQLModule  = module {

    single<SqlDriver> {
        AndroidSqliteDriver(ArticlesDB.Schema, get(), "ArticlesDB.db")
    }

}