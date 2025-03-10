package com.datahiveorg.donetik.util

import android.util.Log
import com.google.firebase.BuildConfig

object Logger {
    fun e(tag: String, message: String) {
        if (BuildConfig.DEBUG) Log.e(tag, message)
    }
}