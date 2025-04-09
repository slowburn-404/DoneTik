package com.datahiveorg.donetik.firebase.model

sealed interface FirebaseRequest {
    data class User(val email: String, val password: String): FirebaseRequest

}