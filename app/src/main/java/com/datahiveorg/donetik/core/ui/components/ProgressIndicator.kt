package com.datahiveorg.donetik.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a circular progress indicator.
 *
 * @param modifier Modifier to be applied to the progress indicator.
 * @param color Color of the progress indicator.
 */
@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    color:Color
) {
    CircularProgressIndicator(
        modifier = modifier
            .size(20.dp),
        strokeCap = StrokeCap.Butt,
        color = color
    )
}