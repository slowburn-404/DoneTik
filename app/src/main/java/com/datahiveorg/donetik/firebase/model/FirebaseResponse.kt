package com.datahiveorg.donetik.firebase.model

import com.google.firebase.auth.FirebaseAuthException

/**
 * Sealed interface of wrap firebase responses
 */
sealed interface FirebaseResponse<out T> {
    data class Success<T>(val data: T): FirebaseResponse<T>
    data class Failure(val exception: FirebaseAuthException): FirebaseResponse<Nothing>
}