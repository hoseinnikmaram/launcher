package com.example.launcher

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object ApplicationContext {
    lateinit var context: Context

    fun initialize(context: Context){
        ApplicationContext.context = context
    }
}