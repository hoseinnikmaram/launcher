package com.boomino.launcher.repository.network
import com.boomino.launcher.model.DataModel

class ApiRepository(private val apiService: ApiService){

     suspend fun getData(): DataModel {
        return apiService.getData()
    }

}