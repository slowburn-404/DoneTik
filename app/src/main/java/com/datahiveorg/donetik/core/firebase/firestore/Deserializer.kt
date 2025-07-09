package com.datahiveorg.donetik.core.firebase.firestore

import android.net.Uri
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
    val authorMap = this["author"] as? Map<*, *>
    val author = authorMap?.let {
        FirebaseDTO.UserDTO(
            uid = it["uid"] as? String ?: "",
            email = it["email"] as? String ?: "",
            username = it["displayName"] as? String ?: "",
            imageUrl = it["photoUrl"] as? Uri ?: Uri.EMPTY
        )
    } ?: throw IllegalArgumentException("Missing or invalid user object")

    return FirebaseDTO.TaskDTO(
        id = getString("id") ?: "",
        title = getString("title") ?: "",
        description = getString("description") ?: "",
        createdAt = getTimestamp("createdAt") ?: Timestamp.now(),
        //TODO: remove lastModified in favor of dueDate when moving to PROD
        dueDate = getTimestamp("dueDate") ?: getTimestamp("lastModified") ?: Timestamp.now(),
        author = author,
        isDone = getBoolean("isDone") ?: false,
        category = getString("category") ?: "Uncategorized"
    )
}