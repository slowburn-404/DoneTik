package com.datahiveorg.donetik.feature.home.presentation.tasklist

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.core.ui.components.AnimatedText
import com.datahiveorg.donetik.core.ui.components.DoneTikSearchBar
import com.datahiveorg.donetik.core.ui.components.TaskSegmentedButtons
import com.datahiveorg.donetik.feature.home.presentation.feed.TaskCard
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    navigator: HomeNavigator,
    snackBar: SnackbarHostState,
    viewModel: TaskListViewModel,
    category: String,
    filterOption: FilterOption
) {
    val state by viewModel.state.collectAsStateWithLifecycle(TaskListState())
    val lazyListState = rememberLazyListState()
    val categoryLazyListState = rememberLazyListState()

    TaskListContent(
        state = state,
        onIntent = viewModel::emitIntent,
        modifier = modifier,
        lazyListState = lazyListState,
        categoryLazyListState = categoryLazyListState,
        categoriesList = state.categories.toList(),
    )

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is TaskListEvent.ShowSnackBar -> {
                    snackBar.showSnackbar(event.message)
                }

                is TaskListEvent.NavigateToTask -> {
                    navigator.navigateToTaskViewScreen(taskId = event.taskId, userId = event.userId)
                }
            }
        }
    }

    LaunchedEffect(category) {
        viewModel.emitIntent(TaskListIntent.SelectCategory(category))

    }

    LaunchedEffect(filterOption) {
        viewModel.emitIntent(TaskListIntent.Filter(filterOption))

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListContent(
    modifier: Modifier = Modifier,
    state: TaskListState,
    onIntent: (TaskListIntent) -> Unit,
    lazyListState: LazyListState,
    categoryLazyListState: LazyListState,
    categoriesList: List<String>,
) {
    PullToRefreshBox(
        onRefresh = {
            onIntent(TaskListIntent.GetTasks)
        },
        isRefreshing = state.isLoading
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState
        ) {
            stickyHeader {

                    DoneTikSearchBar(
                        query = state.query,
                        onSearch = {
                            onIntent(FeedIntent.Search)
                        },
                        searchResults = searchState.searchResults,
                        onQueryChange = { query ->
                            onIntent(FeedIntent.EnterQuery(query))
                        },
                        isExpanded = searchState.isSearchBarExpanded,
                        onExpandedChanged = {
                            onIntent(FeedIntent.ToggleSearchBar)
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.Search,
                                contentDescription = "Search"
                            )
                        },
                        onSearchResultClick = {
                            onIntent(FeedIntent.Search)
                        },
                        placeholder = "Search Tasks"
                    )

            }

            item {
                TaskSegmentedButtons(
                    selectedIndex = state.currentFilterOption,
                    onOptionsSelected = { newStatus ->
                        onIntent(TaskListIntent.Filter(newStatus))
                    },
                    options = FilterOption.entries,
                )

            }

            item {
                CategoriesRow(
                    modifier = Modifier.fillMaxWidth(),
                    categories = categoriesList,
                    lazyListState = categoryLazyListState,
                    selectedCategory = state.selectedCategory,
                    onCategoryClick = { category ->
                        onIntent(TaskListIntent.SelectCategory(category))
                    }
                )
            }


            state.displayedTasks.forEach { (date, tasks) ->
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
                            onIntent(
                                TaskListIntent.SelectTask(task)
                            )
                        },
                    )
                }
            }
        }
    }
}