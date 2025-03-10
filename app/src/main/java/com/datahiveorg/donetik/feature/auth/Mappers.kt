package com.datahiveorg.donetik.feature.auth

import android.net.Uri
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.firebase.model.FirebaseRequest
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

fun User.toUserCredential(): FirebaseRequest.User {
    return FirebaseRequest.User(
        email = email,
        password = password
    )
}

fun AuthResult.toDomain(): User {
    return User(
        uid = user?.uid!!,
        email = user?.email ?: "Email not found",
        username = user?.displayName ?: "",
        imageUrl = user?.photoUrl ?: Uri.EMPTY,
        password = ""
    )
}

fun FirebaseUser.toDomain(): User {
    return User(
        uid = uid,
        email = email!!, // can never be null since sign up requires email
        username = uid,
        imageUrl = photoUrl ?: Uri.EMPTY,
        password = ""
    )
}