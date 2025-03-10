package com.datahiveorg.donetik.firebase.model

sealed interface FirebaseResponse<out T> {
    data class Success<T>(val data: T): FirebaseResponse<T>
    data class Failure(val exception: Exception): FirebaseResponse<Nothing>
}