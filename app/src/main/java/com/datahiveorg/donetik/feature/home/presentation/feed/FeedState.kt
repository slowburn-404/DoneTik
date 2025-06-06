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
    val title: String = "",
    val carouselItems: Set<CarouselItem> = emptySet(),
)

@Stable
@Immutable
data class FilterState(
    val filteredTasks: GroupedTasks = emptyMap(),
    val filter: FilterOption = FilterOption.ALL
)

enum class FilterOption {
    ALL, ACTIVE, DONE
}

@Stable
@Immutable
data class SearchState(
    val searchResults: List<Task> = emptyList(),
    val isSearchBarExpanded: Boolean = false,
    val query: String = "",
)
