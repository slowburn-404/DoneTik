package com.datahiveorg.donetik.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.util.Animation.ANIMATION_DURATION_LONG

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    isEnabled: Boolean,
    isLoading: Boolean
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        enabled = isEnabled,
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn(animationSpec = tween(ANIMATION_DURATION_LONG)),
                exit = fadeOut(animationSpec = tween(ANIMATION_DURATION_LONG)),

            ) {
                AnimatedContent(
                    targetState = label,
                    transitionSpec = {
                        fadeIn(
                            animationSpec = tween(ANIMATION_DURATION_LONG)
                        ) togetherWith fadeOut(
                            animationSpec = tween(ANIMATION_DURATION_LONG)
                        )
                    }
                ) { targetLabel ->
                    Text(
                        text = targetLabel,
                        style = typography.bodyLarge,
                    )
                }
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(animationSpec = tween(ANIMATION_DURATION_LONG)),
                exit = fadeOut(animationSpec = tween(ANIMATION_DURATION_LONG)),
            ) {
                ProgressIndicator(
                    color = colorScheme.onPrimary
                )
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
                ProgressIndicator(color = colorScheme.primary)
            }
        }
    }
}