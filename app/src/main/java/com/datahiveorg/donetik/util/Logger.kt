package com.datahiveorg.donetik.util

import android.util.Log
import com.google.firebase.BuildConfig

object Logger {
    fun e(tag: String, message: String) {
         Log.e(tag, message)
    }

    fun i(tag: String, message: String) {
        if (!BuildConfig.DEBUG) Log.i(tag, message)
    }
}