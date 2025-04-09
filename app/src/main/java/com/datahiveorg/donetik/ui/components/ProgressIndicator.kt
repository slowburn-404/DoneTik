package com.datahiveorg.donetik.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .padding(8.dp)
            .size(16.dp),
        strokeCap = StrokeCap.Butt,
        color = colorScheme.primary
    )
}