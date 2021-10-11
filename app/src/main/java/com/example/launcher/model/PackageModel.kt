package com.example.launcher.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

class PackageModel(val icon: Drawable, val label: String, val packageName:String)