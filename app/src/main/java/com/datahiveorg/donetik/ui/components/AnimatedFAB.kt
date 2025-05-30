package com.datahiveorg.donetik.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.util.Animation.ANIMATION_DURATION_SHORT

/**
 * A Floating Action Button (FAB) that animates its appearance and disappearance.
 *
 * The FAB uses a scale and fade animation for both enter and exit transitions.
 *
 * Example usage:
 * ```kotlin
 * var isFabVisible by remember { mutableStateOf(true) }
 *
 * Scaffold(
 *     floatingActionButton = {
 *         AnimatedFAB(
 *             onClick = {
 *                 // Handle FAB click
 *             },
 *             isVisible = isFabVisible
 *         )
 *     }
 * ) { paddingValues ->
 *     // Content of your screen
 *     // You can toggle `isFabVisible` based on scroll state or other conditions.
 *     // For example, hide FAB when scrolling down:
 *     // val listState = rememberLazyListState()
 *     // isFabVisible = !listState.isScrollInProgress
 * }
 * ```
 *
 * @param modifier Optional [Modifier] to be applied to the FAB.
 * @param onClick Lambda to be invoked when the FAB is clicked.
 * @param isVisible Boolean indicating whether the FAB should be visible or not.
 *                  Defaults to `false`.
 */
@Composable
fun AnimatedFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isVisible: Boolean = false
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            tween(
                durationMillis = ANIMATION_DURATION_SHORT
            )
        ) + fadeIn(),
        exit = scaleOut(
            tween(
                durationMillis = ANIMATION_DURATION_SHORT
            )
        ) + fadeOut()
    ) {
        FloatingActionButton(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(14.dp),
            containerColor = colorScheme.primary,
            contentColor = colorScheme.onPrimary,
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Create new task"
            )
        }
    }
}