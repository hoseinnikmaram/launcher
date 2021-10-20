package com.boomino.launcher.repository.network

import com.boomino.launcher.model.DataModel
import retrofit2.http.GET

interface ApiService {

    @GET("")
   suspend fun getData(): DataModel
}