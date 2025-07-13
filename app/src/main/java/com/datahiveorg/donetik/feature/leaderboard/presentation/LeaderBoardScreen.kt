package com.datahiveorg.donetik.feature.leaderboard.presentation

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.feature.leaderboard.presentation.navigation.LeaderBoardNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LeaderBoardScreen(
    snackBarHostState: SnackbarHostState,
    navigator: LeaderBoardNavigator,
    viewModel: LeaderBoardViewModel
) {
    val state = viewModel.state.collectAsStateWithLifecycle(LeaderBoardState())
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is LeaderBoardEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }

    }

    LeaderBoardContent(
        state = state.value,
        onIntent = viewModel::emitIntent,
        context = context
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderBoardContent(
    modifier: Modifier = Modifier,
    state: LeaderBoardState,
    onIntent: (LeaderBoardIntent) -> Unit,
    context: Context
) {
    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = {
            onIntent(LeaderBoardIntent.FetchLeaderBoard)
        }
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = state.leaderBoardItems,
                key = { it.uid }
            ) { leaderBoardItem ->
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(if (leaderBoardItem.imageUrl == Uri.EMPTY) null else leaderBoardItem.imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_profile),
                    fallback = painterResource(R.drawable.ic_profile),
                )

                LeaderBoardItemCard(
                    modifier = Modifier.fillMaxWidth(),
                    leaderBoardItem = leaderBoardItem,
                    isCurrentUser = leaderBoardItem.uid == state.user.uid,
                    painter = painter
                )
            }
        }
    }
}