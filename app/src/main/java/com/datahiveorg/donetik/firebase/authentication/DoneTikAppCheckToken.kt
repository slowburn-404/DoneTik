package com.datahiveorg.donetik.firebase.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.AppCheckProvider
import com.google.firebase.appcheck.AppCheckToken

// Implement app check token provider for production

//class DoneTikAppCheckToken(
//    private val token: String,
//    private val expiration: Long
//): AppCheckToken() {
//    override fun getToken(): String =  token
//
//    override fun getExpireTimeMillis(): Long = expiration
//}
//
//class DoneTikAppCheckTokenProvider(
//    firebaseApp: FirebaseApp
//): AppCheckProvider{
//    override fun getToken(): TaskDTO<AppCheckToken> {
//        val expMillis =
//    }
//
//}