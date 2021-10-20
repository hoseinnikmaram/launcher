package com.boomino.launcher.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "package_table")
class PackageModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val icon: ByteArray,
    @ColumnInfo(name = "label")
    val label: String,
    @ColumnInfo(name = "packageName")
    val packageName: String
) : Parcelable