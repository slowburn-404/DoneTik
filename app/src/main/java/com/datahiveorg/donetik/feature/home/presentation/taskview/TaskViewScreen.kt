package com.datahiveorg.donetik.feature.home.presentation.taskview

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.core.ui.components.BottomSheetOptions
import com.datahiveorg.donetik.core.ui.components.OptionsBottomSheet
import com.datahiveorg.donetik.core.ui.components.PrimaryButton
import com.datahiveorg.donetik.core.ui.components.ScreenTitle
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskViewScreen(
    viewModel: TaskViewModel,
    navigator: HomeNavigator,
    snackBarHostState: SnackbarHostState,
    taskId: String,
    userId: String
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val bottomSheetText = when (state.task.isDone) {
        true -> "Mark as undone"
        false -> "Mark as done"
    }


    val bottomSheetOptions = listOf(
        BottomSheetOptions(
            icon = R.drawable.ic_delete,
            label = "Delete"
        ),
        BottomSheetOptions(
            icon = R.drawable.ic_done,
            label = bottomSheetText
        )
    )


    LaunchedEffect(taskId, userId) {
        viewModel.emitIntent(TaskViewIntent.GetTask(taskId, userId))
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is TaskViewEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                is TaskViewEvent.NavigateUp -> navigator.navigateUp()
            }
        }
    }



    TaskViewContent(
        state = state,
        onIntent = viewModel::emitIntent,
        scrollState = scrollState
    )


    if (state.showBottomSheet) {
        OptionsBottomSheet(
            onDismiss = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
                viewModel.emitIntent(TaskViewIntent.ToggleBottomSheet)
            },
            options = bottomSheetOptions,
            onOptionsClicked = { bottomSheetOption ->
                when (bottomSheetOption.label) {
                    "Delete" -> {
                        viewModel.emitIntent(TaskViewIntent.DeleteTask(state.task))
                        viewModel.emitIntent(TaskViewIntent.ToggleBottomSheet)
                    }

                    "Mark as done" -> {
                        viewModel.emitIntent(TaskViewIntent.ToggleDoneStatus(state.task))
                        viewModel.emitIntent(TaskViewIntent.ToggleBottomSheet)
                    }

                    "Mark as undone" -> {
                        viewModel.emitIntent(TaskViewIntent.ToggleDoneStatus(state.task))
                        viewModel.emitIntent(TaskViewIntent.ToggleBottomSheet)
                    }

                }

            },
            sheetState = bottomSheetState
        )
    }


}

@Composable
fun TaskViewContent(
    modifier: Modifier = Modifier,
    state: TaskViewState,
    onIntent: (TaskViewIntent) -> Unit,
    scrollState: ScrollState
) {

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
            text = "Due date",
            style = typography.labelSmall,
            color = Color.Gray
        )

        Text(
            text = state.task.dueDate,
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
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
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