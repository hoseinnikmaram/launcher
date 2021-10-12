package com.example.launcher.util

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri


fun checkBrowser(packageManager: PackageManager?):String? {
    val browserIntent = Intent("android.intent.action.VIEW", Uri.parse("http://"))
    val resolveInfo = packageManager?.resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY)
    return resolveInfo?.activityInfo?.packageName
}