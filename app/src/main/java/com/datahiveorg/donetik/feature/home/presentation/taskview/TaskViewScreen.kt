package com.datahiveorg.donetik.feature.home.presentation.taskview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import com.datahiveorg.donetik.ui.components.PrimaryButton
import com.datahiveorg.donetik.ui.components.ScreenTitle
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskViewScreen(
    viewModel: TaskViewModel,
    navigator: HomeNavigator,
    snackBarHostState: SnackbarHostState,
    taskId: String,
    userId: String
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(taskId, userId) {
        viewModel.emitIntent(TaskViewIntent.GetTask(taskId, userId))
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is TaskViewEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }



    TaskViewContent(
        state = state,
        onEvent = viewModel::emitEvent,
        onIntent = viewModel::emitIntent
    )

}

@Composable
fun TaskViewContent(
    modifier: Modifier = Modifier,
    state: TaskViewState,
    onEvent: (TaskViewEvent) -> Unit,
    onIntent: (TaskViewIntent) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ScreenTitle(
            title = state.task.title
        )

        Text(
            text = "Last modified",
            style = typography.labelSmall,
            color = Color.Gray
        )

        Text(
            text = state.task.lastModified,
            style = typography.bodyMedium
        )

        Text(
            text = "Description",
            style = typography.labelSmall,
            color = Color.Gray
        )

        Text(
            text = state.task.description,
            style = typography.bodyLarge
        )

        Text(
            text = "Created",
            style = typography.labelSmall,
            color = Color.Gray
        )

        Text(
            text = state.task.createdAt,
            style = typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        PrimaryButton(
            modifier = Modifier.padding(20.dp),
            label = if (state.task.isDone) "Mark as undone" else "Mark as done",
            onClick = {
                onIntent(TaskViewIntent.ToggleDoneStatus(state.task))
            },
            isEnabled = !state.isLoading,
            isLoading = state.isLoading
        )

    }

}

//@Preview(showBackground = true)
//@Composable
//fun TaskViewContentPreview() {
//    MaterialTheme {
//        val fakeUser = User(
//            uid = "1",
//            email = "fakeuser@fakedomain.com",
//            username = "Fake User",
//            imageUrl = Uri.EMPTY,
//            password = ""
//        )
//
//        val fakeTask = Task(
//            "t1",
//            fakeUser,
//            "Buy groceries",
//            "Milk, Bread, Eggs",
//            false,
//            "1235Hrs 01 Dec 2025",
//            "1240Hrs 01 Dec 2025"
//        )
//
//
//        TaskViewContent(
//            onEvent = {},
//            onIntent = {},
//            state = TaskViewState(
//                task = fakeTask
//            )
//        )
//    }
//}