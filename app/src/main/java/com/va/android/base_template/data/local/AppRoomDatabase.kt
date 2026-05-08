package com.va.android.base_template.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.va.android.base_template.data.local.dao.AppDAO
import com.va.android.base_template.data.model.AppModel

@Database(
    entities = [AppModel::class],
    version = 1
)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun appDao(): AppDAO

    companion object {
        const val DATABASE_NAME = "app_database"

        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getInstance(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    DATABASE_NAME
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}