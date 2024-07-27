package com.example.articleskmp.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.articleskmp.db.ArticlesDB
import org.koin.dsl.module


actual  val  driverSQLModule  = module {
    single<SqlDriver> {
        NativeSqliteDriver(ArticlesDB.Schema, "ArticlesDB.db")
    }
}