package com.datahiveorg.donetik.feature.home.data


import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.firebase.model.FirebaseRequest.TaskDTO
import com.datahiveorg.donetik.firebase.model.FirebaseRequest.UserDTO
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun TaskDTO.toDomain(): Task {
    return Task(
        id = id,
        author = author.toDomain(),
        title = title,
        description = description,
        isDone = isDone,
        createdAt = createdAt.toDate().toDomain(),
        lastModified = lastModified.toDate().toDomain()
    )
}

fun Throwable.toDomain(): String {
    return this.message.toString()
}

fun Task.toFireBase(): Map<String, Any> = mapOf(
    "id" to id,
    "author" to author.toUserDTO(),
    "title" to title,
    "description" to description,
    "isDone" to isDone,
    "createdAt" to createdAt.toFireStoreTimeStamp(),
    "lastModified" to lastModified.toFireStoreTimeStamp()

)

fun User.toUserDTO(): Map<String, Any> {
    return mapOf(
        "uid" to uid,
        "username" to username,
        "email" to email,
        "imageUrl" to imageUrl
    )
}

fun UserDTO.toDomain(): User {
    return User(
        uid = uid,
        username = username,
        email = email,
        imageUrl = imageUrl,
        password = ""
    )
}

fun Date.toDomain(): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(this)
}

fun String.toDate(): Date {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(this)!! // TODO(remember to handle null dates)
}

fun String.toFireStoreTimeStamp(): Timestamp {
    return Timestamp(this.toDate())
}