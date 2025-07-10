package com.datahiveorg.donetik.core.ui.components

import android.content.Context
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.datahiveorg.donetik.R


/**
 * A composable function that loads an image asynchronously and displays it in a circular shape.
 *
 * This function utilizes [AnimatedVisibility] to control the visibility of the image
 * with specified enter and exit transitions. The image is clipped to a [CircleShape].
 *
 * @param modifier The [Modifier] to be applied to the layout. Defaults to [Modifier].
 * @param painter The [AsyncImagePainter] used to load and display the image.
 * @param isVisible A boolean indicating whether the image should be visible. Defaults to `true`.
 * @param enterTransition The [EnterTransition] to apply when the image becomes visible.
 * @param exitTransition The [ExitTransition] to apply when the image becomes invisible.
 */
@Composable
fun AsyncImageLoader(
    modifier: Modifier = Modifier,
    painter: AsyncImagePainter,
    isVisible: Boolean = true,
    enterTransition: EnterTransition = scaleIn(),
    exitTransition: ExitTransition = scaleOut(),
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = enterTransition,
        exit = exitTransition
    ) {
        Box(
            modifier = modifier.clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.clip(CircleShape),
                painter = painter,
                contentDescription = "User profile image",
                contentScale = ContentScale.Crop
            )
        }
    }

}