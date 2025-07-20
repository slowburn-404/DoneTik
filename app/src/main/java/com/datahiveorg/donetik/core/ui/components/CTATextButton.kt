package com.datahiveorg.donetik.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CTATextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color = Color.Gray,
    text: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd
    ) {
        TextButton(
            onClick = onClick
        ) {
            Text(
                text = text,
                style = typography.labelLarge,
                color = color
            )
        }
    }
}