package com.datahiveorg.donetik.ui.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun ProgressIndicator() {
    CircularProgressIndicator(
        strokeCap = StrokeCap.Butt,
    )
}