package com.boomino.launcher.ui.MainFragment

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap.CompressFormat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import com.boomino.launcher.model.PackageModel
import com.boomino.launcher.repository.room.Database
import com.boomino.launcher.ui.MainActivity
import com.boomino.launcher.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class MainViewModel(val database: Database) : ViewModel() {
    private val _responsePackageList = MutableLiveData<List<PackageModel>>()
    val responsePackageList: LiveData<List<PackageModel>>
        get() = _responsePackageList
    init {
        getInstalledPackage()
    }
    fun getInstalledPackage() {
        viewModelScope.launch(Dispatchers.IO) {
            val packages = database.getDao().getPackages()
            _responsePackageList.postValue(packages)
        }
    }

    fun saveToDataBase(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            val packageModel = mutableListOf<PackageModel>()
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val pkgAppsList = activity.packageManager.queryIntentActivities(mainIntent, 0)
            pkgAppsList.forEach {
                val bitmap = it.loadIcon(activity.packageManager).toBitmap()
                val blob = ByteArrayOutputStream()
                bitmap.compress(CompressFormat.PNG, 0, blob)
                if (!isVpnApplication(it, activity))
                    packageModel.add(
                        PackageModel(
                            icon = blob.toByteArray(),
                            label = it.loadLabel(activity.packageManager).toString(),
                            packageName = it.activityInfo.packageName
                        )
                    )
            }
            val database = database.getDao()
            if (database.getPackages().size != packageModel.size) {
                database.deletePackages()
                database.insertPackage(packageModel)
                getInstalledPackage()
            }

        }
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