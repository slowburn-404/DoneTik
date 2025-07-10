package com.datahiveorg.donetik.feature.leaderboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.datahiveorg.donetik.core.ui.components.AsyncImageLoader
import com.datahiveorg.donetik.feature.leaderboard.domain.LeaderBoardItem

@Composable
fun LeaderBoardItemCard(
    modifier: Modifier = Modifier,
    leaderBoardItem: LeaderBoardItem,
    isCurrentUser: Boolean,
    painter: AsyncImagePainter
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser) {
                colorScheme.primary
            } else {
                colorScheme.surface
            },
            contentColor = if (isCurrentUser) {
                colorScheme.onPrimary
            } else {
                colorScheme.onSurface
            }
        )
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImageLoader(
                modifier = modifier.weight(1f),
                painter = painter,
            )

            Column(
                modifier = modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = leaderBoardItem.username,
                    style = typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = leaderBoardItem.points.toString(),
                    style = typography.labelLarge,
                    maxLines = 1,
                    color = Color.Gray
                )
            }
        }
    }
}