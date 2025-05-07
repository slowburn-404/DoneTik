package com.datahiveorg.donetik.feature.home.presentation.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import com.datahiveorg.donetik.ui.components.BottomSheetOptions
import com.datahiveorg.donetik.ui.components.FeedSegmentedButtons
import com.datahiveorg.donetik.ui.components.OptionsBottomSheet
import com.datahiveorg.donetik.ui.components.ScreenTitle
import com.datahiveorg.donetik.util.Logger
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    navigator: HomeNavigator,
    snackBarHostState: SnackbarHostState
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle(initialValue = FeedState())
    val uiEvent by viewModel.event.collectAsStateWithLifecycle(initialValue = FeedEvent.None)
    val filterState by viewModel.filteredTasks.collectAsStateWithLifecycle(initialValue = FilterState())
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val bottomSheetOptions = listOf(
        BottomSheetOptions(
            icon = R.drawable.ic_delete,
            label = "Delete"
        ),
        BottomSheetOptions(
            icon = R.drawable.ic_done,
            label = "Change complete status"
        )
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiEvent) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is FeedEvent.Navigate.Feed -> {
                    navigator.navigateToFeedScreen()
                }

                is FeedEvent.Navigate.NewTask -> {
                    navigator.navigateToNewTaskScreen()
                }

                is FeedEvent.SelectTask -> {
                    Logger.i(
                        "FeedEvent.SelectTask",
                        "Task clicked: ${event.taskId} \n ${event.userId}"
                    )
                    navigator.navigateToTaskViewScreen(taskId = event.taskId, userId = event.userId)
                }

                is FeedEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(event.message)
                }

                is FeedEvent.ToggleOptionsBottomSheet -> {
                    viewModel.toggleOptionsBottomSheet()
                    if (uiState.showBottomSheet) {
                        bottomSheetState.show()
                    } else {
                        bottomSheetState.hide()
                    }
                }

            }
        }
    }

    FeedContent(
        state = uiState,
        onEvent = viewModel::emitEvent,
        onIntent = viewModel::emitIntent,
        filterState = filterState
    )

    if (uiState.showBottomSheet) {
        OptionsBottomSheet(
            sheetState = bottomSheetState,
            onDismiss = {
                viewModel.toggleOptionsBottomSheet()
            },
            options = bottomSheetOptions,
            coroutineScope = coroutineScope,
            onOptionsClicked = { bottomSheetOption ->
                uiState.selectedTask?.let { task ->
                    when (bottomSheetOption.label) {
                        "Delete" -> {
                            viewModel.emitIntent(FeedIntent.Delete(task))
                            viewModel.emitEvent(FeedEvent.ToggleOptionsBottomSheet)
                        }

                        "Change complete status" -> {
                            viewModel.emitIntent(FeedIntent.ToggleDoneStatus(task))
                            viewModel.emitEvent(FeedEvent.ToggleOptionsBottomSheet)
                        }
                    }
                }

            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedContent(
    modifier: Modifier = Modifier,
    state: FeedState,
    filterState: FilterState,
    onEvent: (FeedEvent) -> Unit,
    onIntent: (FeedIntent) -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        onRefresh = {
            onIntent(FeedIntent.GetTasks(state.user.uid))
        },
        isRefreshing = state.isLoading,
        state = pullToRefreshState
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ScreenTitle(
                    title = state.title
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your tasks",
                        style = typography.titleLarge,
                        textAlign = TextAlign.Start
                    )

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    FeedSegmentedButtons(
                        selectedIndex = filterState.filter,
                        onOptionsSelected = { newStatus ->
                            onIntent(FeedIntent.Filter(newStatus))
                        },
                        options = Status.entries
                    )
                }
            }

            items(
                items = filterState.filteredTasks,
                key = { task -> task.id }
            ) { task ->
                TaskCard(
                    modifier = Modifier.animateItem(),
                    task = task,
                    onClick = {
                        Logger.i(
                            "Feed item click",
                            "Task clicked: ${task.id} \n ${task.author.uid}"
                        )
                        onEvent(
                            FeedEvent.SelectTask(
                                taskId = task.id,
                                userId = task.author.uid
                            )
                        )
                    },
                    onLongClick = {
                        onEvent(FeedEvent.ToggleOptionsBottomSheet)
                        onIntent(FeedIntent.SelectTask(task))
                    }
                )
            }
        }
    }
}