package com.va.android.base_template.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AppModel")
data class AppModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int
)
