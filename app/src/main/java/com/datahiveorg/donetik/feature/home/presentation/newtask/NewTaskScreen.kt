package com.datahiveorg.donetik.feature.home.presentation.newtask

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.feature.home.data.toHomeDomain
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import com.datahiveorg.donetik.ui.components.DoneTikDatePicker
import com.datahiveorg.donetik.ui.components.DoneTikTimePicker
import com.datahiveorg.donetik.ui.components.InputFieldDialog
import com.datahiveorg.donetik.ui.components.PrimaryButton
import com.datahiveorg.donetik.ui.components.UserInputField
import com.datahiveorg.donetik.ui.components.rememberCalendarInstance
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskScreen(
    viewModel: NewTaskViewModel,
    navigator: HomeNavigator,
    snackBarHostState: SnackbarHostState
) {
    val state by viewModel.state.collectAsStateWithLifecycle(initialValue = NewTaskState())

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is NewTaskEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(event.message)
                }

                is NewTaskEvent.SaveSuccessful -> {
                    navigator.navigateUp()
                }
            }
        }
    }

    NewTaskContent(
        state = state,
        onIntent = viewModel::emitIntent,
        scrollState = scrollState
    )

    InputFieldDialog(
        onDismiss = {
            viewModel.emitIntent(NewTaskIntent.ToggleDialog)

        },
        onSaveClick = {
            viewModel.emitIntent(NewTaskIntent.EnterCategory(it))
        },
        confirmButtonText = "Save",
        title = "Save category",
        label = null,
        showDialog = state.showCategoryDialog,
    )

    DoneTikDatePicker(
        onDismiss = {
            viewModel.emitIntent(NewTaskIntent.ToggleDatePicker)
        },
        onSelectDate = { datePickerState ->
            datePickerState.selectedDateMillis?.let {
                viewModel.emitIntent(NewTaskIntent.EnterDate(it))
            }
        },
        showDateTimePicker = state.showDatePicker
    )

    DoneTikTimePicker(
        onDismiss = {
            viewModel.emitIntent(NewTaskIntent.ToggleTimePicker)
        },
        onSelectTime = { timePickerState ->
            viewModel.emitIntent(
                NewTaskIntent.EnterTime(
                    hour = timePickerState.hour,
                    minute = timePickerState.minute
                )
            )
        },
        showTimePicker = state.showTimePicker,
    )


}

@Composable
fun NewTaskContent(
    modifier: Modifier = Modifier,
    state: NewTaskState,
    onIntent: (NewTaskIntent) -> Unit,
    scrollState: ScrollState
) {
    val calendar = rememberCalendarInstance()
    val selectedDate by remember(state.selectedDate) {
        derivedStateOf {
            state.selectedDate?.let {
                calendar.timeInMillis = it
                calendar.time.toHomeDomain().substringBefore(",")
            } ?: ""
        }
    }
    val selectedTime by remember(state.selectedHour, state.selectedMinute) {
        derivedStateOf {
            if (state.selectedHour != null && state.selectedMinute != null) {
                "${state.selectedHour}: ${state.selectedMinute}"
            } else {
                ""
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AssistChip(
            onClick = {
                onIntent(NewTaskIntent.ToggleDialog)
            },
            shape = shapes.extraLarge,
            label = {
                Text(
                    text = state.task.category,
                    style = typography.labelLarge,
                )
            },
        )

        UserInputField(
            modifier = Modifier.fillMaxWidth(),
            label = "Title",
            enterValue = { titleInput ->
                onIntent(NewTaskIntent.EnterTitle(titleInput))
            },
            onTogglePasswordVisibility = {},
            error = state.titleError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            leadingIcon = null,
            trailingIcon = null,
            placeholder = "Title",
            value = state.task.title,
            visualTransformation = VisualTransformation.None,
        )

        UserInputField(
            modifier = Modifier.fillMaxWidth(),
            label = "Description",
            enterValue = { description ->
                onIntent(NewTaskIntent.EnterDescription(description))
            },
            onTogglePasswordVisibility = {},
            error = state.descriptionError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            leadingIcon = null,
            trailingIcon = null,
            placeholder = "Description",
            value = state.task.description,
            visualTransformation = VisualTransformation.None,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UserInputField(
                modifier = Modifier
                    .weight(1f),
                label = "Due date",
                enterValue = {},
                value = selectedDate,
                onTogglePasswordVisibility = {},
                error = state.selectedDateError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                leadingIcon = painterResource(R.drawable.ic_teams),
                trailingIcon = null,
                placeholder = "Due date",
                visualTransformation = VisualTransformation.None,
                isReadOnly = true,
                onClick = {
                    onIntent(NewTaskIntent.ToggleDatePicker)
                }
            )

            UserInputField(
                modifier = Modifier
                    .weight(1f),
                label = "Time",
                enterValue = {},
                value = selectedTime,
                onTogglePasswordVisibility = {},
                error = state.selectedTimeError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                leadingIcon = painterResource(R.drawable.ic_teams),
                trailingIcon = null,
                placeholder = "Time",
                visualTransformation = VisualTransformation.None,
                isReadOnly = true,
                onClick = {
                    onIntent(NewTaskIntent.ToggleTimePicker)
                }
            )

        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        PrimaryButton(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            label = "Save",
            onClick = {
                onIntent(NewTaskIntent.CreateTask)
            },
            isEnabled = state.isFormValid,
            isLoading = state.isLoading
        )
    }
}