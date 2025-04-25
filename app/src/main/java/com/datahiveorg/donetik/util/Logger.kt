package com.datahiveorg.donetik.util

import android.util.Log

object Logger {
    fun e(tag: String, message: String) {
        //if (BuildConfig.DEBUG) TODO(Change to production)
        Log.e(tag, message)
    }

    fun i(tag: String, message: String) {
        //if (!BuildConfig.DEBUG)
        Log.i(tag, message)
    }
}