package com.ingatkoding.blog.android

import android.app.Application
import com.ingatkoding.blog.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidContext(this@Application)
            modules(commonModule, androidModule)
        }
    }
}