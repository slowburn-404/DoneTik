package com.datahiveorg.donetik.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.datahiveorg.donetik.R

/**
 * A composable function that displays a Lottie animation as a loading indicator.
 *
 * This function uses the Lottie library to render an animation from a raw resource file.
 * The animation is set to iterate forever, creating a continuous loading effect.
 * The animation is centered within a Box that fills the maximum available size.
 *
 * @param modifier Optional [Modifier] to be applied to the loading animation container.
 *                 Defaults to [Modifier].
 */
@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.handloading)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

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