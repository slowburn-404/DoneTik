package com.datahiveorg.donetik.feature.auth

import android.net.Uri
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.firebase.model.FirebaseRequest
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser

fun User.toUserCredential(): FirebaseRequest.UserDTO {
    return FirebaseRequest.UserDTO(
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

fun FirebaseAuthException.toDomain(): String {
    return when(this.errorCode) {
        "ERROR_INVALID_EMAIL" -> "Invalid email format."
        "ERROR_WRONG_PASSWORD" -> "Incorrect password."
        "ERROR_USER_NOT_FOUND" -> "No account found with this email."
        "ERROR_USER_DISABLED" -> "This account has been disabled."
        "ERROR_TOO_MANY_REQUESTS" -> "Too many failed attempts. Try again later."
        "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Check your connection and try again."
        else -> "Authentication failed: ${this.message}"
    }
}