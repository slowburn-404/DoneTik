package com.datahiveorg.donetik.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.ui.navigation.FeatureScreen

@Composable
fun FAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
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