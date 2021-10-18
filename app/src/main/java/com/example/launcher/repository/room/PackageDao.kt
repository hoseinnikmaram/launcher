package com.example.launcher.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.launcher.model.PackageModel

@Dao
interface PackageDao {
    @Insert
    suspend fun insertPackage(packageModel: List<PackageModel>)
    @Query("SELECT * FROM package_table")
    suspend fun getPackages():List<PackageModel>
    @Query("DELETE FROM package_table")
    suspend fun deletePackages()
}