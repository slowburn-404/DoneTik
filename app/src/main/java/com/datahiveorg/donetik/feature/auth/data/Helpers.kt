package com.datahiveorg.donetik.feature.auth.data

import android.net.Uri
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.core.firebase.model.FirebaseRequest
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser

/**
 * Converts a [User] object to a [FirebaseRequest.CredentialsDTO] object.
 *
 * @return A [FirebaseRequest.CredentialsDTO] object containing the user's email and password.
 */
fun User.toUserCredential(): FirebaseRequest.CredentialsDTO {
    return FirebaseRequest.CredentialsDTO(
        email = email,
        password = password
    )
}

/**
 * Converts a FirebaseUser object to a User object.
 *
 * @return The User object.
 */
fun FirebaseUser.toAuthDomain(): User {
    return User(
        uid = uid,
        email = email!!, // can never be null since sign up requires email
        username = uid,
        imageUrl = photoUrl ?: Uri.EMPTY,
        password = ""
    )
}

/**
 * Converts a [Throwable] to a user-friendly error message string.
 *
 * This function handles specific Firebase Authentication exceptions and provides
 * corresponding messages. For other exceptions, it returns a generic error message.
 *
 * @return A [String] representing the error message.
 */
fun Throwable.toAuthDomain(): String {
    return when (this) {
        is FirebaseAuthWeakPasswordException -> "Please pick a stronger password."
        is FirebaseAuthInvalidCredentialsException -> "Invalid email or password."
        is FirebaseAuthInvalidUserException -> "No account found with this email."
        is FirebaseAuthUserCollisionException -> "This email address is already in use by another account"
        is FirebaseAuthEmailException -> "Invalid email address."
        is FirebaseAuthMultiFactorException -> "Multi-factor authentication is required."
        is FirebaseAuthException -> "Something went wrong, please try again."
        else -> "Something went wrong, please try again."
    }
}