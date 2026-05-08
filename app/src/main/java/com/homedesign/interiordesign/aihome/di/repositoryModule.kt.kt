package com.homedesign.interiordesign.aihome.di

import com.homedesign.interiordesign.aihome.data.repository.AppRemoteRepository
import com.homedesign.interiordesign.aihome.data.repository.AppRoomRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AppRoomRepository(get()) }
    single { AppRemoteRepository(get()) }
}