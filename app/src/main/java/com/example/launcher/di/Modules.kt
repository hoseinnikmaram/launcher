package com.example.launcher.di

import com.example.launcher.network.ApiRepository
import com.example.launcher.ui.MainFragment.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule= module {
    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { ApiRepository(get()) }
}

val viewModelsModule= module {

    viewModel {
        MainViewModel(get())
    }
}

