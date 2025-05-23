package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import com.datahiveorg.donetik.util.Animation

fun homeEnterTransition(): EnterTransition =
    scaleIn() + fadeIn(
        animationSpec = tween(
            durationMillis = Animation.ANIMATION_DURATION_SHORT,
            easing = LinearEasing
        )
    )


fun homeExitTransition(): ExitTransition =
    scaleOut() + fadeOut(
        animationSpec = tween(
            durationMillis = Animation.ANIMATION_DURATION_SHORT,
            easing = LinearEasing
        )
    )
