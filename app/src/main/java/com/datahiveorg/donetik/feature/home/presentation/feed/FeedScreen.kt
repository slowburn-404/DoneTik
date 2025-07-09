package com.datahiveorg.donetik.feature.home.presentation.feed

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.core.ui.components.AnimatedText
import com.datahiveorg.donetik.core.ui.components.BottomSheetOptions
import com.datahiveorg.donetik.core.ui.components.FeedSegmentedButtons
import com.datahiveorg.donetik.core.ui.components.OptionsBottomSheet
import com.datahiveorg.donetik.core.ui.components.ScreenTitle
import com.datahiveorg.donetik.core.ui.components.SecondaryButton
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    navigator: HomeNavigator,
    snackBarHostState: SnackbarHostState
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle(initialValue = FeedState())
    val filterState by viewModel.filteredTasks.collectAsStateWithLifecycle(initialValue = FilterState())
    val pullToRefreshState = rememberPullToRefreshState()

    val carouselState = rememberCarouselState { uiState.carouselItems.count() }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is FeedEvent.Navigate.Feed -> navigator.navigateToFeedScreen()
                is FeedEvent.Navigate.NewTask -> navigator.navigateToNewTaskScreen()
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
        filterState = filterState,
        pullToRefreshState = pullToRefreshState,
        carouselState = carouselState,
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
    pullToRefreshState: PullToRefreshState,
    carouselState: CarouselState
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
//                item {
//                    DoneTikSearchBar(
//                        query = searchState.query,
//                        onSearch = {
//                            onIntent(FeedIntent.Search)
//                        },
//                        searchResults = searchState.searchResults,
//                        onQueryChange = { query ->
//                            onIntent(FeedIntent.EnterQuery(query))
//                        },
//                        isExpanded = searchState.isSearchBarExpanded,
//                        onExpandedChanged = {
//                            onIntent(FeedIntent.ToggleSearchBar)
//                        },
//                        leadingIcon = {
//                            Icon(
//                                Icons.Rounded.Search,
//                                contentDescription = "Search"
//                            )
//                        },
//                        onSearchResultClick = {
//                            onIntent(FeedIntent.Search)
//                        },
//                        placeholder = "Search Tasks"
//                    )
//                }

                item {
                    ScreenTitle(
                        title = state.title
                    )
                }

                item {
                    Text(
                        text = "Categories",
                        style = typography.titleLarge,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.SemiBold
                    )
                }


                item {
                    StatsCarousel(
                        carouselItems = state.carouselItems,
                        carouselState = carouselState
                    )
                }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        TextButton(
                            onClick = {
                                onEvent(FeedEvent.ShowSnackBar("Coming soon"))
                            }
                        ) {
                            Text(
                                text = "Show all",
                                style = typography.labelLarge
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Your Tasks",
                        style = typography.titleLarge,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                item {
                    FeedSegmentedButtons(
                        selectedIndex = filterState.filter,
                        onOptionsSelected = { newStatus ->
                            onIntent(FeedIntent.Filter(newStatus))
                        },
                        options = FilterOption.entries,
                    )

                }

                filterState.filteredTasks.forEach { (date, tasks) ->
                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(colorScheme.primaryContainer)
                        ) {

                            AnimatedText(
                                text = date,
                                style = typography.bodyMedium,
                                color = colorScheme.onPrimaryContainer,
                                transitionSpec = {
                                    slideInVertically { it } + fadeIn() togetherWith
                                            slideOutVertically { -it } + fadeOut()
                                }
                            )
                        }
                    }

                    items(
                        items = tasks,
                        key = { task -> task.id }
                    ) { task ->
                        TaskCard(
                            modifier = Modifier.animateItem(),
                            task = task,
                            onClick = {
                                onEvent(
                                    FeedEvent.SelectTask(
                                        taskId = task.id,
                                        userId = task.author.uid
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}