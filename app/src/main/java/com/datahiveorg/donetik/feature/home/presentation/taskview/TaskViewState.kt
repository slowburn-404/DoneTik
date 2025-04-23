package com.datahiveorg.donetik.feature.home.presentation.taskview

import android.net.Uri
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.domain.model.Task

data class TaskViewState(
    val task: Task = Task(
        id = "",
        isDone = false,
        title = "",
        createdAt = "",
        description = "",
        lastModified = "",
        author = User(
            uid = "",
            username = "",
            email = "",
            imageUrl = Uri.EMPTY,
            password = ""
        )
    ),
    val isLoading: Boolean = false,
    val error: String = ""

)
