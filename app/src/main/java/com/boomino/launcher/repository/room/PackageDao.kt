package com.boomino.launcher.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.boomino.launcher.model.PackageModel

@Dao
interface PackageDao {
    @Insert
    suspend fun insertPackage(packageModel: List<PackageModel>)
    @Query("SELECT * FROM package_table")
    suspend fun getPackages():List<PackageModel>
    @Transaction
    @Query("DELETE FROM package_table")
    suspend fun deletePackages()
    @Query("SELECT * FROM package_table WHERE label LIKE :label")
    fun getPackagesBySearch(label: String): LiveData<List<PackageModel>>
}