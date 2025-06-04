package com.datahiveorg.donetik.feature.home.presentation.feed

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.feature.home.domain.model.Task

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = shapes.extraLarge,
    ) {
        //TODO(Use constraint layout instead)
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = task.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )

                if (task.isDone) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = "Is done"
                    )
                }
            }

            Text(
                text = task.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = typography.bodyLarge
            )
            Text(
                text = task.createdAt,
                style = typography.bodySmall
            )

        }

    }
}