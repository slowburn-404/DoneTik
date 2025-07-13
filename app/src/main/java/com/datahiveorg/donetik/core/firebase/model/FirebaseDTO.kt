package com.datahiveorg.donetik.core.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

/**
 * Sealed interface to wrap data transfer objects for Firebase operations.
 *
 * This interface defines the different types of data that can be sent to or received from Firebase.
 * Each data class represents a specific entity or data structure used in the application.
 *
 * - [Credentials]: Represents user credentials (email and password) for authentication.
 * - [TaskDTO]: Represents a task item with its details.
 * - [UserDTO]: Represents a user profile with their details.
 */
sealed interface FirebaseDTO {
    data class Credentials(
        val email: String,
        val password: String
    ) : FirebaseDTO

    data class TaskDTO(
        val id: String,
        val author: String,
        val title: String,
        val description: String,
        @field: JvmField val isDone: Boolean,
        @ServerTimestamp val createdAt: Timestamp,
        @ServerTimestamp val dueDate: Timestamp,
        val category: String
    ) : FirebaseDTO

    data class UserDTO(
        val uid: String,
        val email: String,
        val username: String,
        val imageUrl: String,
    ): FirebaseDTO

    data class LeaderBoardUserDTO(
        val uid: String,
        val username: String,
        val points: Long,
        val imageUrl: String
    ): FirebaseDTO
}