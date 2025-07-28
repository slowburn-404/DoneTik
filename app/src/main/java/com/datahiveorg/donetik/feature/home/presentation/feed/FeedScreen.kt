package com.datahiveorg.donetik.feature.home.presentation.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.core.ui.components.CTATextButton
import com.datahiveorg.donetik.core.ui.components.ScreenTitle
import com.datahiveorg.donetik.core.ui.components.SecondaryButton
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import com.datahiveorg.donetik.feature.home.presentation.tasklist.FilterOption
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    navigator: HomeNavigator,
    snackBarHostState: SnackbarHostState
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle(initialValue = FeedState())
    val pendingTasks by viewModel.pendingTasks.collectAsStateWithLifecycle(initialValue = emptyList())
    val pullToRefreshState = rememberPullToRefreshState()

    val carouselState = rememberCarouselState { uiState.carouselItems.count() }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is FeedEvent.Navigate.Feed -> navigator.navigateToFeedScreen()
                is FeedEvent.Navigate.NewTask -> navigator.navigateToNewTaskScreen()
                is FeedEvent.Navigate.TaskList -> navigator.navigateToTaskListScreen(
                    category = event.category,
                    filterOption = event.filterOption
                )

                is FeedEvent.SelectTask -> navigator.navigateToTaskViewScreen(
                    taskId = event.taskId,
                    userId = event.userId
                )

                is FeedEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)


            }
        }
    }

    FeedContent(
        state = uiState,
        onEvent = viewModel::emitEvent,
        onIntent = viewModel::emitIntent,
        pullToRefreshState = pullToRefreshState,
        carouselState = carouselState,
        pendingTasks = pendingTasks,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedContent(
    modifier: Modifier = Modifier,
    state: FeedState,
    onEvent: (FeedEvent) -> Unit,
    onIntent: (FeedIntent) -> Unit,
    pullToRefreshState: PullToRefreshState,
    carouselState: CarouselState,
    pendingTasks: List<Task>,
) {

    PullToRefreshBox(
        onRefresh = {
            onIntent(FeedIntent.Refresh)
        },
        isRefreshing = state.isLoading,
        state = pullToRefreshState
    ) {

        if (state.tasks.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                ScreenTitle(
                    title = "Such empty, create a few tasks"
                )

                androidx.compose.animation.AnimatedVisibility(
                    visible = !state.isLoading
                ) {
                    SecondaryButton(
                        label = "Refresh",
                        onClick = {
                            onIntent(FeedIntent.Refresh)
                        },
                        leadingIcon = null,
                        isLoading = false,
                    )
                }
            }
        } else {
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pending Tasks",
                            style = typography.titleLarge,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.SemiBold
                        )

                        CTATextButton(
                            onClick = {
                                onEvent(
                                    FeedEvent.Navigate.TaskList(
                                        category = "",
                                        filterOption = FilterOption.PENDING
                                    )
                                )
                            },
                            text = "Show all"
                        )
                    }
                }

                items(
                    items = pendingTasks,
                    key = { it.id }
                ) { task ->
                    TaskCard(
                        modifier = Modifier.animateItem(),
                        task = task,
                        onClick = {
                            onEvent(
                                FeedEvent.SelectTask(
                                    taskId = task.id,
                                    userId = task.author
                                )
                            )
                        },
                    )
                }

                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Categories",
                            style = typography.titleLarge,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }


                item {
                    StatsCarousel(
                        carouselItems = state.carouselItems,
                        carouselState = carouselState,
                        onCarouselItemClick = { carouselItem ->
                            onEvent(
                                FeedEvent.Navigate.TaskList(
                                    category = carouselItem.category,
                                    filterOption = FilterOption.ALL
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}