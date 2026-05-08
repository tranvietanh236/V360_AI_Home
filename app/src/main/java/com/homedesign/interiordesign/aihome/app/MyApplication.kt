package com.homedesign.interiordesign.aihome.app

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.homedesign.interiordesign.aihome.di.appModule
import com.homedesign.interiordesign.aihome.di.dbModule
import com.homedesign.interiordesign.aihome.di.repositoryModule
import com.homedesign.interiordesign.aihome.di.viewModelModule
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