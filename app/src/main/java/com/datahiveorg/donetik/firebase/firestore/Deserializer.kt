package com.datahiveorg.donetik.firebase.firestore

import android.net.Uri
import com.datahiveorg.donetik.firebase.model.FirebaseRequest
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot


internal fun DocumentSnapshot.toTaskDTO(): FirebaseRequest.TaskDTO {
    val authorMap = this["author"] as? Map<*, *>
    val author = authorMap?.let {
        FirebaseRequest.UserDTO(
            uid = it["uid"] as? String ?: "",
            email = it["email"] as? String ?: "",
            username = it["displayName"] as? String ?: "",
            imageUrl = it["photoUrl"] as? Uri ?: Uri.EMPTY
        )
    } ?: throw IllegalArgumentException("Missing or invalid user object")

    return FirebaseRequest.TaskDTO(
        id = getString("id") ?: "",
        title = getString("title") ?: "",
        description = getString("description") ?: "",
        createdAt = getTimestamp("createdAt") ?: Timestamp.now(),
        lastModified = getTimestamp("lastModified") ?: Timestamp.now(),
        author = author,
        isDone = getBoolean("isDone") ?: false
    )
}