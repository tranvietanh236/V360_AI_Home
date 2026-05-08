package com.va.android.base_template.app

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.va.android.base_template.di.appModule
import com.va.android.base_template.di.dbModule
import com.va.android.base_template.di.repositoryModule
import com.va.android.base_template.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            startKoin {
                androidContext(this@MyApplication)
                modules(listOf(appModule, dbModule, repositoryModule, viewModelModule))
            }
        }catch (e: Exception){
            Log.d("MyApplication", "onCreate: $e")
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}