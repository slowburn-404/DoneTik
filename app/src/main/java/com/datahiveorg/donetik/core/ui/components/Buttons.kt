package com.datahiveorg.donetik.core.ui.components

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
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.feature.home.presentation.tasklist.FilterOption
import com.datahiveorg.donetik.util.Animation.ANIMATION_DURATION_SHORT

/**
 * A composable function that displays a primary button with a label, an onClick action,
 * and states for enabled and loading.
 *
 * @param modifier The modifier to be applied to the button.
 * @param label The text to be displayed on the button.
 * @param onClick The lambda function to be executed when the button is clicked.
 * @param isEnabled A boolean value indicating whether the button is enabled or disabled.
 * @param isLoading A boolean value indicating whether the button is in a loading state.
 *                  When true, a progress indicator is shown instead of the label.
 */
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

/**
 * A composable function that displays a secondary button with an optional leading icon.
 *
 * @param modifier The modifier to be applied to the button.
 * @param label The text to be displayed on the button.
 * @param onClick The callback to be invoked when the button is clicked.
 * @param leadingIcon The painter for the icon to be displayed before the label. Can be null if no icon is needed.
 * @param isLoading A boolean indicating whether the button is in a loading state. If true, a progress indicator is shown instead of the label and icon.
 */
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

/**
 * A composable function that displays a row of segmented buttons for filtering options.
 *
 * @param modifier The modifier to be applied to the component.
 * @param onOptionsSelected A callback function that is invoked when an option is selected.
 * @param options A list of [FilterOption] objects representing the available filter options.
 * @param selectedIndex The currently selected [FilterOption].
 */
@Composable
fun TaskSegmentedButtons(
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

/**
 * A composable function that displays a text button with customizable text, click action, and color.
 *
 * @param modifier The modifier to be applied to the button.
 * @param text The text to be displayed on the button.
 * @param onClick The lambda function to be executed when the button is clicked.
 * @param color The color of the text on the button. Defaults to the primary color from the MaterialTheme.
 */
@Composable
fun DoneTikTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    color: Color = colorScheme.primary
) {
    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            text = text,
            color = color,
            style = typography.bodyLarge
        )
    }
}