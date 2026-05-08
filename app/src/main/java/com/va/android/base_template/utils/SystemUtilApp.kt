package com.va.android.base_template.utils

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import java.util.Locale

object SystemUtilApp {
    private var myLocale: Locale? = null
    private const val KEY_REGION_NAME = "KEY_REGION_NAME"
    private const val DEFAULT_LANGUAGE_CODE = "en"
    const val RATE = "RATE"
    const val GUIDE = "GUIDE"
    private const val PREF_NAME = "data"

    fun saveRegionName(context: Context, regionName: String?) {
        val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        preferences.edit {
            putString(KEY_REGION_NAME, regionName ?: "")
        }
    }

    fun getRegionName(context: Context): String? {
        val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return preferences.getString(KEY_REGION_NAME, "American")
    }

    // Lưu ngôn ngữ đã cài đặt
    fun saveLocale(context: Context, lang: String?) {
        setPreLanguage(context, lang ?: DEFAULT_LANGUAGE_CODE)
    }

    fun setLocale(context: Context) {
        val language = getPreLanguage(context)
        if (language == "") {
            val config = Configuration()
            val locale = Locale.getDefault()
            Locale.setDefault(locale)
            config.locale = locale
            context.resources
                .updateConfiguration(config, context.resources.displayMetrics)
        } else {
            changeLang(language, context)
        }
    }

    // method phục vụ cho việc thay đổi ngôn ngữ.
    private fun changeLang(lang: String?, context: Context) {
        if (lang.equals("", ignoreCase = true)) return
        myLocale = Locale(lang)
        saveLocale(context, lang)
        Locale.setDefault(myLocale)
        val config = Configuration()
        config.locale = myLocale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun getPreLanguage(mContext: Context): String? {
        val preferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return preferences.getString("KEY_LANGUAGE", "en")
    }

    private fun setPreLanguage(context: Context, language: String?) {
        if (language != null && language != "") {
            val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("KEY_LANGUAGE", language)
            editor.apply()
        }
    }

    fun setInt(context: Context, nameString: String, index: Int) {
        val pre = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = pre.edit()
        editor.putInt(nameString, index)
        editor.apply()
    }

    fun getInt(context: Context, nameString: String): Int {
        val pre = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pre.getInt(nameString, 0)
    }

    /*lưu các biến kiểu String*/
    fun setString(context: Context, nameString: String, url: String) {
        val pre = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = pre.edit()
        editor.putString(nameString, url)
        editor.apply()
    }

    fun getString(context: Context, nameString: String): String? {
        val pre = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pre.getString(nameString, null)
    }

    fun isRated(context: Context): Boolean {
        val pre = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pre.getBoolean(RATE, false)
    }

    fun forceRated(context: Context) {
        val pre = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pre.edit(commit = true) {
            putBoolean(RATE, true)
        }
    }

    fun setBoolean(context: Context, nameString: String, url: Boolean) {
        val pre = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = pre.edit()
        editor.putBoolean(nameString, url)
        editor.apply()
    }

    fun getBoolean(context: Context, nameString: String): Boolean {
        val pre = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pre.getBoolean(nameString, false)
    }
}
