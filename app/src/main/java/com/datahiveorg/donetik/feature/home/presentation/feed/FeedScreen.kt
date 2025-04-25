package com.datahiveorg.donetik.feature.home.presentation.feed

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen
import com.datahiveorg.donetik.ui.components.ScreenTitle
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = koinViewModel(),
    navigator: DoneTikNavigator,
    snackBarHostState: SnackbarHostState
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

            is FeedEvent.ShowSnackBar -> {
                snackBarHostState.showSnackbar((event as FeedEvent.ShowSnackBar).message)
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
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
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

}

//@Preview(showBackground = true)
//@Composable
//fun FeedContentPreview() {
//    val fakeUser = User(
//        uid = "1",
//        email = "fakeuser@fakedomain.com",
//        username = "Fake User",
//        imageUrl = Uri.EMPTY,
//        password = ""
//    )
//    val fakeTasks = listOf(
//        Task(
//            "t1",
//            fakeUser,
//            "Buy groceries",
//            "Milk, Bread, Eggs",
//            false,
//            "2025-04-20T10:00:00",
//            "2025-04-20T10:00:00"
//        ),
//        Task(
//            "t2",
//            fakeUser,
//            "Morning run",
//            "5km jog around the park",
//            true,
//            "2025-04-18T07:30:00",
//            "2025-04-19T08:00:00"
//        ),
//        Task(
//            "t3",
//            fakeUser,
//            "Write blog post",
//            "Topic: Kotlin Coroutines",
//            false,
//            "2025-04-17T15:00:00",
//            "2025-04-17T15:00:00"
//        ),
//    )
//    val fakeState = FeedState(
//        tasks = fakeTasks
//    )
//    MaterialTheme {
//        FeedContent(
//            onEvent = {},
//            onIntent = {},
//            state = fakeState
//        )
//    }
//}