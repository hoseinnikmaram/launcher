package com.nikmaram.launcher.di

import com.nikmaram.launcher.repository.network.ApiRepository
import com.nikmaram.launcher.ui.MainFragment.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule= module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { ApiRepository(get()) }
    single { provideRoomClient(get()) }
}

val viewModelsModule= module {

    viewModel {
        MainViewModel(get())
    }
}

