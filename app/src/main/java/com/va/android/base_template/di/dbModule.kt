package com.va.android.base_template.di

import android.content.Context
import com.va.android.base_template.data.local.AppRoomDatabase
import org.koin.dsl.module

val dbModule = module {
    single { provideDatabase(get()) }
    single { provideAppDao(get()) }
}

private fun provideDatabase(context: Context): AppRoomDatabase {
    return AppRoomDatabase.getInstance(context)
}
private fun provideAppDao(database: AppRoomDatabase) = database.appDao()