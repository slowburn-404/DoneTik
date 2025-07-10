package com.datahiveorg.donetik.feature.home.presentation.newtask

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import coil3.toCoilUri
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.data.toHomeDomain
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
        createdAt = Date().toHomeDomain(),
        dueDate = Date().toHomeDomain(),
        category = "Uncategorized"
    ),
    val error: String = "",
    val titleError: String = "",
    val descriptionError: String = "",
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val showCategoryDialog: Boolean = false,
    val selectedDate: Long? = null,
    val selectedHour: Int? = null,
    val selectedMinute: Int? = null,
    val selectedTimeError: String = "",
    val selectedDateError: String = "",
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
)
