package com.datahiveorg.donetik.feature.router

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun RouterScreen(
    modifier: Modifier = Modifier,
    viewModel: RouterViewModel = koinViewModel(),
    onNavigate: (FeatureScreen) -> Unit
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.handloading)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is RouterEvent.Navigate -> {
                    delay(1500L) //getting user info is asynchronous TODO(Find better solution)
                    onNavigate(
                        event.screen
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = {
                progress
            }
        )
    }
}