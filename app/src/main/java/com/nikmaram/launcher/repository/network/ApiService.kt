package com.nikmaram.launcher.repository.network

import com.nikmaram.launcher.model.DataModel
import retrofit2.http.GET

interface ApiService {

    @GET("")
   suspend fun getData(): DataModel
}