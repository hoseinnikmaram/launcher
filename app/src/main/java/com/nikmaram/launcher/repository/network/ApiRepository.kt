package com.nikmaram.launcher.repository.network
import com.nikmaram.launcher.model.DataModel

class ApiRepository(private val apiService: ApiService){

     suspend fun getData(): DataModel {
        return apiService.getData()
    }

}