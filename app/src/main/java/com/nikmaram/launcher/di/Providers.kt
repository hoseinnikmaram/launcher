package com.nikmaram.launcher.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nikmaram.launcher.repository.network.ApiService
import com.nikmaram.launcher.repository.room.Database
import com.nikmaram.launcher.ui.MainActivity
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val base_url = ""

fun provideRetrofit(httpClient: OkHttpClient): ApiService {

    return Retrofit.Builder()
        .baseUrl(base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(ApiService::class.java)
}


fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

}

fun provideRoomClient(context: Context): Database {
    return Room.databaseBuilder(
        context,
        Database::class.java,
        "launcher"
    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
}
