package com.datahiveorg.donetik.feature.teams.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.datahiveorg.donetik.ui.components.ScreenTitle

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ScreenTitle(title = "LeaderBoard")
    }

}