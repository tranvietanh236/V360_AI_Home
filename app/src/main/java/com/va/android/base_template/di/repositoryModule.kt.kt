package com.va.android.base_template.di

import com.va.android.base_template.data.repository.AppRemoteRepository
import com.va.android.base_template.data.repository.AppRoomRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AppRoomRepository(get()) }
    single { AppRemoteRepository(get()) }
}