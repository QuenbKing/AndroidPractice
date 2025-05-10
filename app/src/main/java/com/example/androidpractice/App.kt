package com.example.androidpractice

import android.app.Application
import com.example.androidpractice.di.dbMoviesModule
import com.example.androidpractice.di.dbProfileModule
import com.example.androidpractice.di.rootModule
import com.example.androidpractice.utils.NotificationUtils
import com.example.androidpractice.web.restModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NotificationUtils.createNotificationChannel(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(rootModule, restModule, dbMoviesModule, dbProfileModule)
        }
    }
}