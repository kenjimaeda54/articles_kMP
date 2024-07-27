package com.example.articleskmp.android

import android.app.Application
import com.example.articleskmp.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
        }
    }


}