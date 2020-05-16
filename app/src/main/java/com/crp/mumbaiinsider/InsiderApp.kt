package com.crp.mumbaiinsider

import android.app.Application
import com.crp.mumbaiinsider.di.networkModule
import com.crp.mumbaiinsider.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class InsiderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@InsiderApp)
            modules(listOf(networkModule, viewModelModule))
        }
    }
}