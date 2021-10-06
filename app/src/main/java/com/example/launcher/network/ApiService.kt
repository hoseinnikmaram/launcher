package com.example.launcher.network

import com.example.launcher.model.DataModel
import retrofit2.http.GET

interface ApiService {

    @GET("")
   suspend fun getData(): DataModel
}