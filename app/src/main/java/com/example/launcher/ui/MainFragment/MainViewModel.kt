package com.example.launcher.ui.MainFragment

import com.example.launcher.util.Resource
import androidx.lifecycle.*
import com.example.launcher.network.ApiRepository
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class MainViewModel(private val apiRepository: ApiRepository) :ViewModel(){

   fun getLiveDataNews()=
           liveData(Dispatchers.IO) {

               emit(Resource.loading(data = null))
               try{
                   emit(Resource.success(data = apiRepository.getData()))
               }
               catch (e:Exception){
                   emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
               }
           }




}