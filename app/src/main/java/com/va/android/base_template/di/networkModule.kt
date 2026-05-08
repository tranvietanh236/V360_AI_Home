package com.va.android.base_template.di

import android.util.Log
import com.va.android.base_template.BuildConfig
import com.va.android.base_template.data.remote.apiservice.AppApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

val networkModule = module {
    single(named("GenerateImageOkHttpClient")) {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

        val errorInterceptor = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if (!response.isSuccessful) {
                val errorBody = response.peekBody(Long.MAX_VALUE).string()
                Log.d("NetworkError", "Mã lỗi Generate: ${response.code}, Nội dung: $errorBody")
            }
            response
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(errorInterceptor)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .build()
    }

    // Category
    single(named("CategoryOkHttpClient")) {
        val categoryLoggingInterceptor = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val errorInterceptor = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            if (!response.isSuccessful) {
                val errorBody = response.peekBody(Long.MAX_VALUE).string()
                Log.d("NetworkError", "Mã lỗi Category: ${response.code}, Nội dung: $errorBody")
            }
            response
        }

        val categoryHeaderInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val modifiedRequest = originalRequest.newBuilder()
                .addHeader("API_KEY", "API_TOKEN")
                .build()
            chain.proceed(modifiedRequest)
        }

        OkHttpClient.Builder()
            .addInterceptor(categoryLoggingInterceptor)
            .addInterceptor(errorInterceptor)
            .addInterceptor(categoryHeaderInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    single(named("Category")) {
        try {
            Retrofit.Builder()
                .baseUrl("BASE_URL")
                .client(get(named("CategoryOkHttpClient")))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        } catch (e: Exception) {
            null
        }

    }

    single { get<Retrofit>(named("Category")).create(AppApi::class.java) }
}