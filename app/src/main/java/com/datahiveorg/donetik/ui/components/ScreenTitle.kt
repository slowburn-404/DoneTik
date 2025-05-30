package com.datahiveorg.donetik.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a screen title.
 *
 * @param modifier The modifier to be applied to the text.
 * @param title The text to be displayed as the title.
 */
@Composable
fun ScreenTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        modifier = modifier.padding(vertical = 4.dp),
        text = title,
        style = MaterialTheme.typography.displayMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.SemiBold
    )

}