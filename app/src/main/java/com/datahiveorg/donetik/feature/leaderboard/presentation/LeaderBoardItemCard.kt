package com.datahiveorg.donetik.feature.leaderboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.datahiveorg.donetik.R
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
            containerColor = if (isCurrentUser) colorScheme.primaryContainer else colorScheme.surface,
            contentColor = if (isCurrentUser) colorScheme.onPrimaryContainer else colorScheme.onSurface
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImageLoader(
                modifier = Modifier.size(36.dp),
                painter = painter,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = leaderBoardItem.username,
                    style = typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )

                Text(
                    text = pluralStringResource(
                        R.plurals.number_of_points,
                        leaderBoardItem.points.toInt(),
                        leaderBoardItem.points.toInt()
                    ),
                    style = typography.bodyLarge,
                    maxLines = 1,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}