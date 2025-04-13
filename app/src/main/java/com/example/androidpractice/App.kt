package com.example.androidpractice

import android.app.Application
import com.example.androidpractice.di.rootModule
import com.example.androidpractice.web.restModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(rootModule, restModule)
        }
    }
}