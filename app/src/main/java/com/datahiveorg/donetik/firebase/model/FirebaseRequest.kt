package com.datahiveorg.donetik.firebase.model

sealed interface FirebaseRequest {
    data class UserDTO(val email: String, val password: String): FirebaseRequest

}