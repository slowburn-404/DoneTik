package com.datahiveorg.donetik.ui.components

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.feature.home.presentation.feed.FilterOption
import com.datahiveorg.donetik.util.Animation.ANIMATION_DURATION_SHORT

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    isEnabled: Boolean,
    isLoading: Boolean
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.primary,
            contentColor = colorScheme.onPrimary
        )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn(animationSpec = tween(ANIMATION_DURATION_SHORT)),
                exit = fadeOut(animationSpec = tween(ANIMATION_DURATION_SHORT)),

                ) {
                AnimatedText(
                    text = label,
                    style = typography.bodyLarge,
                    transitionSpec = {
                        fadeIn(tween(ANIMATION_DURATION_SHORT)) togetherWith fadeOut(
                            tween(
                                ANIMATION_DURATION_SHORT
                            )
                        )
                    },
                )

            }

            androidx.compose.animation.AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(animationSpec = tween(ANIMATION_DURATION_SHORT)),
                exit = fadeOut(animationSpec = tween(ANIMATION_DURATION_SHORT)),
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
        modifier = modifier,
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

@Composable
fun FeedSegmentedButtons(
    modifier: Modifier = Modifier,
    onOptionsSelected: (FilterOption) -> Unit,
    options: List<FilterOption>,
    selectedIndex: FilterOption,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, filterOption ->
            SegmentedButton(
                onClick = { onOptionsSelected(filterOption) },
                selected = filterOption == selectedIndex,
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
            ) {
                Text(
                    //TODO: hoist this up the ui tree as state with `rememberSaveAble`since it is a calculation
                    text = filterOption.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = typography.bodyLarge
                )
            }
        }

    }

}