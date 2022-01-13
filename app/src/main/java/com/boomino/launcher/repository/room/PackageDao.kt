package com.boomino.launcher.repository.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.boomino.launcher.model.PackageModel

@Dao
interface PackageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackage(packageModel: List<PackageModel>)
    @Query("SELECT * FROM package_table")
    fun getPackages(): LiveData<List<PackageModel>>
    @Transaction
    @Query("DELETE FROM package_table")
    suspend fun deletePackages()
    @Query("SELECT * FROM package_table WHERE label LIKE :label")
    fun getPackagesBySearch(label: String): LiveData<List<PackageModel>>
}