package com.datahiveorg.donetik.feature.home.presentation.feed

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.domain.model.Task

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp)),
        onClick = onClick
    ) {
        //TODO(Use constraint layout instead)
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = task.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.titleMedium
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
                style = typography.bodyMedium
            )
            Text(
                text = task.createdAt,
                style = typography.bodySmall
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    val task = Task(
        id = "1",
        title = "Task title",
        description = "Task description",
        isDone = true,
        createdAt = "10th Jan 2025",
        lastModified = "",
        author = User(
            "",
            "",
            "",
            Uri.EMPTY,
            "",
        )
    )
    MaterialTheme {
        TaskCard(
            task = task,
        ) { }
    }
}