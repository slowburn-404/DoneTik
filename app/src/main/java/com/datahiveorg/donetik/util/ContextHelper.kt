package com.datahiveorg.donetik.util

import android.content.Context

suspend fun Context.signOut(googleSignHelper: GoogleSignHelper) {
    googleSignHelper.clearCredentials()
}