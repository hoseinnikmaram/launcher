package com.example.launcher.di

import com.example.launcher.network.ApiService
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val base_url=""

 fun provideRetrofit(httpClient: OkHttpClient): ApiService {

    return Retrofit.Builder()
        .baseUrl(base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(ApiService::class.java)
        }


    fun provideOkHttpClient(loggingInterceptor: LoggingInterceptor):OkHttpClient{
        return OkHttpClient.Builder()
            .connectTimeout(5,TimeUnit.SECONDS)
            .readTimeout(5,TimeUnit.SECONDS)
            .writeTimeout(5,TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

    }

fun provideLoggingInterceptor(): LoggingInterceptor {

    return LoggingInterceptor.Builder()
        .setLevel(Level.BASIC)
        .log(Platform.INFO)
        .addHeader("version","1")
        .build()
}
