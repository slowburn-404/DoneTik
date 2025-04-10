package com.datahiveorg.donetik.router

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import kotlinx.coroutines.delay

@Composable
fun RouterScreen(
    event: RouterEvent,
    modifier: Modifier = Modifier,
    onNavigate: (FeatureScreen) -> Unit
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.handloading)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LaunchedEffect(event) {
        when (event) {
            is RouterEvent.Navigate -> {
                delay(2000L) //mbogi lazima waone animation
                onNavigate(
                    event.screen
                )
            }

            is RouterEvent.None -> {}
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