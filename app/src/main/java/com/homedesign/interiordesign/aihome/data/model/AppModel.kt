package com.homedesign.interiordesign.aihome.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AppModel")
data class AppModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int
)
