package com.datahiveorg.donetik.feature.home.presentation.feed

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.domain.model.Task

@Stable
@Immutable
data class FeedState(
    val tasks: List<Task> = emptyList(),
    val selectedTask: Task? = null,
    val isLoading: Boolean = false,
    val error: String = "",
    val user: User = User(
        uid = "",
        username = "",
        email = "",
        imageUrl = Uri.EMPTY,
        password = ""
    ),
    val title: String = ""
)

data class FilterState(
    val filteredTasks: List<Task> = emptyList(),
    val filter: Status = Status.ACTIVE
)

enum class Status {
    ACTIVE, DONE
}
