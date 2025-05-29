package com.datahiveorg.donetik.feature.home.presentation.newtask

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import com.datahiveorg.donetik.ui.components.InputFieldDialog
import com.datahiveorg.donetik.ui.components.PrimaryButton
import com.datahiveorg.donetik.ui.components.UserInputField

@Composable
fun NewTaskScreen(
    viewModel: NewTaskViewModel,
    navigator: HomeNavigator,
    snackBarHostState: SnackbarHostState
) {
    val state by viewModel.state.collectAsStateWithLifecycle(initialValue = NewTaskState())

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
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
        dismissButtonText = "Dismiss",
        title = "Save category",
        label = null,
        showDialog = state.showCategoryDialog,
    )

}

@Composable
fun NewTaskContent(
    modifier: Modifier = Modifier,
    state: NewTaskState,
    onIntent: (NewTaskIntent) -> Unit,
    scrollState: ScrollState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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