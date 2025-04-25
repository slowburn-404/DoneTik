package com.datahiveorg.donetik.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isVisible: Boolean = false
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        FloatingActionButton(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(14.dp),
            containerColor = colorScheme.primaryContainer,
            contentColor = colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Create new task"
            )
        }
    }
}