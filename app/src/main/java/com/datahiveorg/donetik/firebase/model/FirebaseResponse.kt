package com.datahiveorg.donetik.firebase.model

import com.google.firebase.auth.FirebaseAuthException


/**
 * Sealed interface to wrap Firebase responses.
 *
 * This interface represents the outcome of a Firebase operation, which can either be a success
 * or a failure.
 *
 * @param T The type of data expected in a successful response.
 */
sealed interface FirebaseResponse<out T> {
    data class Success<T>(val data: T): FirebaseResponse<T>
    data class Failure(val exception: FirebaseAuthException): FirebaseResponse<Nothing>
}