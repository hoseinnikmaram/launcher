package com.example.launcher.network
import com.example.launcher.model.DataModel

class ApiRepository(private val apiService: ApiService){

     suspend fun getData(): DataModel {
        return apiService.getData()
    }

}