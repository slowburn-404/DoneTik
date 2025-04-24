package com.datahiveorg.donetik.feature.home.presentation.feed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen
import com.datahiveorg.donetik.ui.components.ScreenTitle
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = koinViewModel(),
    navigator: DoneTikNavigator,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle(initialValue = FeedState())
    val event by viewModel.event.collectAsStateWithLifecycle(initialValue = FeedEvent.None)

    LaunchedEffect(event) {
        when (event) {
            is FeedEvent.None -> {}
            is FeedEvent.Navigate.NewTask -> {
                navigator.navigate(HomeScreen.NewTaskScreen)
            }

            is FeedEvent.SelectTask -> {
                navigator.navigate(
                    HomeScreen.TaskScreen(
                        taskId = (event as FeedEvent.SelectTask).taskId,
                        userId = (event as FeedEvent.SelectTask).userId
                    )
                )
            }

        }
    }

    FeedContent(
        state = uiState,
        onEvent = viewModel::emitEvent,
        onIntent = viewModel::emitIntent,
    )


}

@Composable
fun FeedContent(
    modifier: Modifier = Modifier,
    state: FeedState,
    onEvent: (FeedEvent) -> Unit,
    onIntent: (FeedIntent) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        item {
            ScreenTitle(
                title = "Feed"
            )
        }

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