package com.datahiveorg.donetik.ui.components

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