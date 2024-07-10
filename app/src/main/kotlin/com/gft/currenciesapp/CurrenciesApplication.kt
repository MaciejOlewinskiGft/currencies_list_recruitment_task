package com.gft.currenciesapp

import android.app.Application
import com.gft.currenciesapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CurrenciesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CurrenciesApplication)

            modules(appModule)
        }
    }
}
