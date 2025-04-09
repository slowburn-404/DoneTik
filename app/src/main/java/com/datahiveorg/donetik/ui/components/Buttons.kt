package com.datahiveorg.donetik.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    isFormValid: Boolean,
    isLoading: Boolean
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        enabled = isFormValid,
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (!isLoading) {
                Text(
                    text = label,
                    style = typography.bodyLarge,
                )
            } else {
                ProgressIndicator()
            }
        }
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    leadingIcon: Painter?,
    isLoading: Boolean
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isLoading) {
                leadingIcon?.let {
                    Icon(
                        painter = it,
                        contentDescription = null
                    )
                }
                Text(
                    text = label,
                    style = typography.bodyLarge
                )
            } else {
                ProgressIndicator()
            }
        }
    }
}