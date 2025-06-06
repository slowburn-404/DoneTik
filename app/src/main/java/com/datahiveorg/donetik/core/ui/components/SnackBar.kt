package com.datahiveorg.donetik.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a Snackbar with a custom message.
 *
 * @param message The message to be displayed in the Snackbar.
 */
@Composable
fun SnackBar(
    message: String
) {
    Snackbar(
        modifier = Modifier
            .padding(16.dp),
        containerColor = colorScheme.primary,
        contentColor = colorScheme.onPrimary,
        shape = RoundedCornerShape(14.dp),
        content = {
            Text(
                modifier = Modifier.padding(4.dp),
                text = message
            )
        }
    )
}