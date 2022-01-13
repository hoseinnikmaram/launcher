package com.nikmaram.launcher.util

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.*

fun <T> liveDataFromValue(value: T): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.value = value!!

    return result
}
fun <T> runAsync(block: suspend CoroutineScope.() -> T) {
    GlobalScope.launch (Dispatchers.Main) {
        block()
    }.invokeOnCompletion {
        if (it != null && (it !is CancellationException)) {
            Handler(Looper.getMainLooper()).post {
                throw it
            }
        }
    }
}