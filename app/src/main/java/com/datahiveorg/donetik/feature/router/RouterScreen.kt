package com.datahiveorg.donetik.feature.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.ui.components.LoadingAnimation
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun RouterScreen(
    modifier: Modifier = Modifier,
    viewModel: RouterViewModel = koinViewModel(),
    onNavigate: (FeatureScreen) -> Unit
) {


    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is RouterEvent.Navigate -> {
                    onNavigate(
                        event.screen
                    )
                }
            }
        }
    }

    LoadingAnimation(
        modifier = modifier
    )
}