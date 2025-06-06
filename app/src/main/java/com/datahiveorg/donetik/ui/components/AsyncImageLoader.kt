package com.datahiveorg.donetik.ui.components

import android.content.Context
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.datahiveorg.donetik.R


/**
 * A composable function that loads an image asynchronously from a URL and displays it in a circular shape.
 *
 * @param modifier The modifier to be applied to the image.
 * @param imageUrl The URL of the image to load.
 * @param context The [Context] to use for loading the image.
 * @param isVisible Whether the image should be visible. Defaults to `true`.
 * @param [EnterTransition] The enter transition for the image.
 * @param [ExitTransition] The exit transition for the image.
 * @param placeholder The resource ID of the placeholder image to display while the image is loading.
 */
@Composable
fun AsyncImageLoader(
    modifier: Modifier = Modifier,
    imageUrl: Uri,
    context: Context,
    isVisible: Boolean = true,
    enterTransition: EnterTransition,
    exitTransition: ExitTransition,
    @DrawableRes placeholder: Int = R.drawable.ic_profile
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(if (imageUrl == Uri.EMPTY) null else imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(placeholder),
    )

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