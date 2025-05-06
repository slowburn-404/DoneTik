package com.datahiveorg.donetik.ui.components

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.datahiveorg.donetik.R


@Composable
fun AsyncImageLoader(
    modifier: Modifier = Modifier,
    imageUrl: Uri?,
    context: Context
) {
    val model = ImageRequest
        .Builder(context)
        .data(imageUrl)
        .crossfade(true)
        .build()

    AsyncImage(
        modifier = modifier.clip(CircleShape),
        model = model,
        placeholder = painterResource(R.drawable.ic_profile),
        contentDescription = "Profile image",
        contentScale = ContentScale.Crop
    )

}