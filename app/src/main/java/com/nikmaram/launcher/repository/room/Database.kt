package com.nikmaram.launcher.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nikmaram.launcher.model.PackageModel

@Database(entities = [PackageModel::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun getDao(): PackageDao
}