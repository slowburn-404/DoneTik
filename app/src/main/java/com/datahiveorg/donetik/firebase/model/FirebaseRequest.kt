package com.datahiveorg.donetik.firebase.model

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

/**
 * Sealed interface to wrap data transfer objects
 */
sealed interface FirebaseRequest {
    data class CredentialsDTO(
        val email: String,
        val password: String
    ) : FirebaseRequest

    data class TaskDTO(
        val id: String,
        val author: UserDTO,
        val title: String,
        val description: String,
        @field: JvmField val isDone: Boolean,
        @ServerTimestamp val createdAt: Timestamp,
        @ServerTimestamp val dueDate: Timestamp,
        val category: String
    ) : FirebaseRequest

    data class UserDTO(
        val uid: String,
        val email: String,
        val username: String,
        val imageUrl: Uri,
    ): FirebaseRequest

}