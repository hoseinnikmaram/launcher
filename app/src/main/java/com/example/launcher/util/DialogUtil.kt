package com.example.launcher.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.launcher.R
import com.example.launcher.databinding.DialogGeneralBinding

fun Context.showDialog(title: String? = null, description: String? = null, negativeCallback: () -> Unit = {}, positiveCallback: () -> Unit = {}): Dialog {
    val dialog = Dialog(this, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val binding: DialogGeneralBinding = DataBindingUtil.inflate(
        LayoutInflater.from(this),
        R.layout.dialog_general,
        null,
        false
    )

    binding.title.text = title
    binding.description.text = description

    dialog.setContentView(binding.root)
    dialog.setCancelable(false)
    val lp = WindowManager.LayoutParams()
    lp.copyFrom(dialog.window!!.attributes)
    binding.btnCancel.setOnClickListener {
        negativeCallback()
        dialog.dismiss()
    }

    binding.btnOk.setOnClickListener {
        positiveCallback()
        dialog.dismiss()
    }

    dialog.show()
    dialog.window!!.attributes = lp
    return dialog
}