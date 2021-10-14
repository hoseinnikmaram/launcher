package com.example.launcher.ui.MainFragment

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.*
import com.example.launcher.model.PackageModel
import com.example.launcher.network.ApiRepository
import com.example.launcher.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainViewModel :ViewModel(){

     fun getInstalledPackage(activity: Activity) =
        liveData(Dispatchers.IO) {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkgAppsList = activity.packageManager.queryIntentActivities(mainIntent, 0)
        emit(pkgAppsList.map {
            PackageModel(
                icon = it.loadIcon(activity.packageManager),
                label = it.loadLabel(activity.packageManager).toString(),
                packageName = it.activityInfo.packageName
            )
        })
    }

    fun isShowDialog(activity: Activity){
        val isShowDialog = defaultCache(activity.applicationContext)[KEY_IS_SHOW_DIALOG, false]
        if (!isShowDialog!!) {
            defaultCache(activity.applicationContext)[KEY_IS_SHOW_DIALOG] = true
            activity.showDialog()
        }
    }
}