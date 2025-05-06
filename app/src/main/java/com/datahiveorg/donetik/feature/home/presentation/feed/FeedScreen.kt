package com.datahiveorg.donetik.feature.home.presentation.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import com.datahiveorg.donetik.ui.components.FeedSegmentedButtons
import com.datahiveorg.donetik.util.Logger
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    navigator: HomeNavigator,
    snackBarHostState: SnackbarHostState
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle(initialValue = FeedState())
    val uiEvent by viewModel.event.collectAsStateWithLifecycle(initialValue = FeedEvent.None)
    val filterState by viewModel.filteredTasks.collectAsStateWithLifecycle(initialValue = FilterState())

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

            }
        }
    }

    FeedContent(
        state = uiState,
        onEvent = viewModel::emitEvent,
        onIntent = viewModel::emitIntent,
        filterState = filterState
    )


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
                FeedSegmentedButtons(
                    selectedIndex = filterState.filter,
                    onOptionsSelected = { newStatus ->
                        onIntent(FeedIntent.Filter(newStatus))
                    },
                    options = Status.entries
                )
            }

            items(
                items = filterState.filteredTasks,
                key = { task -> task.id }
            ) { task ->
                TaskCard(
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
                    }
                )

            }
        }
    }

}