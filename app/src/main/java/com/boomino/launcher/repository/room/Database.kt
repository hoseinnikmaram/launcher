package com.boomino.launcher.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boomino.launcher.model.PackageModel

@Database(entities = [PackageModel::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun getDao(): PackageDao
}