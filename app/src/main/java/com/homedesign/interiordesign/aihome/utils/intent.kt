package com.homedesign.interiordesign.aihome.utils


import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.core.os.BundleCompat
import com.google.gson.Gson

inline fun <reified T> Intent.getParcelable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        extras?.let { BundleCompat.getParcelable(it, key, T::class.java) }
    } else {
        getParcelableExtra(key)
    }
}

inline fun <reified T : Parcelable> Intent.getParcelableArrayList(key: String): ArrayList<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        extras?.let { BundleCompat.getParcelableArrayList(it, key, T::class.java) }
    } else {
        getParcelableArrayListExtra(key)
    }
}

inline fun <reified T> String.convertToObject(): T = Gson().fromJson(this, T::class.java)