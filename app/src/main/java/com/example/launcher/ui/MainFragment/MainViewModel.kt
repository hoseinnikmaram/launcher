package com.example.launcher.ui.MainFragment

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.lifecycle.*
import com.example.launcher.model.PackageModel
import com.example.launcher.util.*
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {

    fun getInstalledPackage(activity: Activity): LiveData<List<PackageModel>> =
        liveData(Dispatchers.IO) {
            val packageModel = mutableListOf<PackageModel>()
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val pkgAppsList = activity.packageManager.queryIntentActivities(mainIntent, 0)
            pkgAppsList.forEach {
                if (!isVpnApplication(it, activity))
                    packageModel.add(PackageModel(
                        icon = it.loadIcon(activity.packageManager),
                        label = it.loadLabel(activity.packageManager).toString(),
                        packageName = it.activityInfo.packageName
                    ))
            }
            emit(packageModel)
        }

    fun isVpnApplication(resolveInfo: ResolveInfo, activity: Activity): Boolean {
        return resolveInfo.activityInfo.packageName.lowercase()
            .contains("vpn") || resolveInfo.loadLabel(activity.packageManager).toString()
            .contains("vpn")
    }

    fun isShowDialog(activity: Activity) {
        val isShowDialog = defaultCache(activity.applicationContext)[KEY_IS_SHOW_DIALOG, false]
        if (!isShowDialog!!) {
            defaultCache(activity.applicationContext)[KEY_IS_SHOW_DIALOG] = true
            activity.showDialog()
        }
    }
}