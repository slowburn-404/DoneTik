package com.datahiveorg.donetik.feature.home.presentation.newtask

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.data.toDomain
import com.datahiveorg.donetik.feature.home.domain.model.Task
import java.util.Date
import java.util.UUID.randomUUID

@Stable
@Immutable
data class NewTaskState(
    val task: Task = Task(
        id = randomUUID().toString(),
        author = User(
            uid = "",
            username = "",
            email = "",
            imageUrl = Uri.EMPTY,
            password = ""
        ),
        title = "",
        description = "",
        isDone = false,
        createdAt = Date().toDomain(),
        lastModified = Date().toDomain()
    ),
    val error: String = ""
)
