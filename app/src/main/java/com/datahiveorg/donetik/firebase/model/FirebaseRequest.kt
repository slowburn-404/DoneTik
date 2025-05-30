package com.datahiveorg.donetik.firebase.model

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

/**
 * Sealed interface to wrap data transfer objects for Firebase operations.
 *
 * This interface defines the different types of data that can be sent to or received from Firebase.
 * Each data class represents a specific entity or data structure used in the application.
 *
 * - [CredentialsDTO]: Represents user credentials (email and password) for authentication.
 * - [TaskDTO]: Represents a task item with its details.
 * - [UserDTO]: Represents a user profile with their details.
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