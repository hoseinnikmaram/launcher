package com.example.launcher.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.provider.Settings
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.launcher.R
import com.example.launcher.model.PackageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun hideKeyboardFrom(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun EditText.setOnEnterListener(listener: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            listener()
            true
        } else {
            false
        }
    }
}

fun View.animateToTop(body: ()-> Unit){
    this.animate()
        .translationY(+this.height.toFloat())
        .alpha(0.0f)
        .setDuration(300)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                body()
            }
        })
}
 fun directOpenInstalledApp(context: Context,packageManager: PackageManager?,packageName: String) {
    val intent = packageManager?.getLaunchIntentForPackage(packageName)
    if (intent != null) {
        context.startActivity(intent);
    }
}

 fun Activity.showDialog() {
    if (AssistantUtil.getCurrentAssistWithReflection(this.applicationContext) != KEY_PACKAGE_NAME_ZAREBIN) {
        this.showDialog(
            getString(R.string.assistant_title),
            getString(R.string.assistant_description),
            positiveCallback = {
                startActivity(Intent(Settings.ACTION_VOICE_INPUT_SETTINGS))
            })
    }
    if (checkBrowser(this.packageManager).toString() != KEY_PACKAGE_NAME_ZAREBIN) {
        this.showDialog(
            getString(R.string.browser_title),
            getString(R.string.browser_description),
            positiveCallback = {
                startActivity(Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS))
            })
    }
}

