package com.datahiveorg.donetik.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays text with an animation when the text content changes.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param text The text to be displayed.
 * @param transitionSpec A lambda that defines the transition animation for the text content change.
 *                       It receives an [AnimatedContentTransitionScope] and should return a [ContentTransform].
 * @param style The text style to be applied to the text. Defaults to [typography.bodyLarge].
 * @param color The color of the text. Defaults to [colorScheme.onPrimary].
 */
@Composable
fun AnimatedText(
    modifier: Modifier = Modifier,
    text: String,
    transitionSpec: AnimatedContentTransitionScope<String>.() -> ContentTransform,
    style: TextStyle = typography.bodyLarge,
    color: Color = colorScheme.onPrimary
) {
    AnimatedContent(
        modifier = modifier.padding(8.dp),
        targetState = text,
        transitionSpec = transitionSpec
    ) { targetLabel ->
        Text(
            text = targetLabel,
            style = style,
            color = color
        )
    }
}