package com.nikmaram.launcher.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.nikmaram.launcher.R

fun hideKeyboardFrom(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


 fun directOpenInstalledApp(context: Context,packageManager: PackageManager?,packageName: String) {
    val intent = packageManager?.getLaunchIntentForPackage(packageName)
    if (intent != null) {
        context.startActivity(intent);
    }
}

fun openInformationApp(context: Context,packageName: String) {

    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.data = Uri.parse("package:$packageName");
        context.startActivity(intent);
    } catch (e:Exception) {
        val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
        context.startActivity(intent);
    }
}

fun String?.normalizePersianDigits(): String {
    if (this.isNullOrEmpty()) return ""
    var result = ""
    var en: Char
    for (ch in this) {
        en = ch
        when (ch) {
            '0' -> en = '۰'
            '1' -> en = '۱'
            '2' -> en = '۲'
            '3' -> en = '۳'
            '4' -> en = '۴'
            '5' -> en = '۵'
            '6' -> en = '۶'
            '7' -> en = '۷'
            '8' -> en = '۸'
            '9' -> en = '۹'
        }
        result = "${result}$en"
    }
    return result
}

fun actionSearch(textSearch: String,context: Context) {
    val text = textSearch.trim().replace(" ", "%20")
    if (text.isEmpty())
        return
    val browserIntent =
        Intent(Intent.ACTION_VIEW, Uri.parse(URL_ZAREBIN + text))
    context.startActivity(browserIntent)
}
fun onLongClickPage(context: Context){
    context.startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
}