package com.datahiveorg.donetik.core.firebase.firestore

import com.datahiveorg.donetik.core.firebase.model.FirebaseDTO
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot


/**
 * Converts a [DocumentSnapshot] from FireStore to a [FirebaseDTO.TaskDTO] object.
 *
 * This extension function deserializes the data from a FireStore document representing a task
 * into a structured DTO. It handles potential missing or invalid fields by providing default values
 * or throwing an [IllegalArgumentException] for critical missing data like the author.
 *
 * @receiver The [DocumentSnapshot] to convert.
 * @return A [FirebaseDTO.TaskDTO] object populated with data from the document.
 * @throws IllegalArgumentException if the "author" field is missing or invalid in the document.
 */
internal fun DocumentSnapshot.toTaskDTO(): FirebaseDTO.TaskDTO {
    return FirebaseDTO.TaskDTO(
        id = getString("id") ?: "",
        title = getString("title") ?: "",
        description = getString("description") ?: "",
        createdAt = getTimestamp("createdAt") ?: Timestamp.now(),
        dueDate = getTimestamp("dueDate") ?: Timestamp.now(),
        author = getString("author") ?: "",
        isDone = getBoolean("isDone") ?: false,
        category = getString("category") ?: "Uncategorized"
    )
}