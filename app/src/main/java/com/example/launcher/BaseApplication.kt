package com.example.launcher

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.launcher.di.applicationModule
import com.example.launcher.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin{
            androidContext(this@BaseApplication)
            modules(applicationModule,viewModelsModule)

        }
    }

}