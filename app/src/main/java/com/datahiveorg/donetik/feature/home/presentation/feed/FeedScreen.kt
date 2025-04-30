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
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.util.Logger

@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    navigator: DoneTikNavigator,
    snackBarHostState: SnackbarHostState
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle(initialValue = FeedState())

    LaunchedEffect(key1 = true) {
        viewModel.event.collect { event ->
            when (event) {
                is FeedEvent.Navigate -> {
                    navigator.navigate(HomeScreen.NewTaskScreen)
                }

                is FeedEvent.SelectTask -> {
                    navigator.navigate(
                        HomeScreen.TaskScreen(
                            taskId = event.taskId,
                            userId = event.userId
                        )
                    )
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
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedContent(
    modifier: Modifier = Modifier,
    state: FeedState,
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
            items(
                items = state.tasks,
                key = { task -> task.id }
            ) { task ->
                TaskCard(
                    task = task,
                    onClick = {
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