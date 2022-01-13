package com.nikmaram.launcher.ui.MainFragment

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap.CompressFormat
import androidx.compose.runtime.mutableStateListOf
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import com.nikmaram.launcher.model.PackageModel
import com.nikmaram.launcher.repository.room.Database
import com.nikmaram.launcher.ui.MainActivity
import com.nikmaram.launcher.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class MainViewModel(val database: Database) : ViewModel() {
    private var _responsePackageDefaultList = MutableLiveData<List<PackageModel>>()
    val responsePackageDefaultList: LiveData<List<PackageModel>>
        get() = _responsePackageDefaultList

    init {
        getDefaultApps()
    }

    fun getInstalledPackage(): LiveData<List<PackageModel>> {
        return database.getDao().getPackages()
    }

    private fun getDefaultApps() {
        val packages = database.getDao().getPackages()
        packages.observeForever { packages ->
            val subListSize = if (packages.size < 4) packages.size else 4
            _responsePackageDefaultList.value = packages.toMutableList().subList(0, subListSize)
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
                packageModel.add(
                    PackageModel(
                        icon = blob.toByteArray(),
                        label = it.loadLabel(activity.packageManager).toString(),
                        packageName = it.activityInfo.packageName
                    )
                )
            }
            val database = database.getDao()
            database.insertPackage(packageModel)
        }
    }


    fun getInstallPackagesBySearch(label: String): LiveData<List<PackageModel>> {
        return database.getDao().getPackagesBySearch("%$label%")
    }
}