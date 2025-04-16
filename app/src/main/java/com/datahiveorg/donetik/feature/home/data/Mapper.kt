package com.datahiveorg.donetik.feature.home.data

import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.auth.toDomain
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.firebase.model.TaskDTO
import com.google.firebase.auth.FirebaseUser

fun TaskDTO.toDomain(): Task {
    return Task(
        id = id,
        author = author,
        title = title,
        description = description,
        isDone = isDone,
        createdAt = created,
        lastModified = lastModified
    )
}

fun Throwable.toDomain(): String {
    return this.message.toString()
}

fun Task.toFireBase(): TaskDTO {
    return TaskDTO(
        author = author,
        id = "",
        title = title,
        description = description,
        isDone = isDone,
        created = createdAt,
        lastModified = lastModified
    )
}