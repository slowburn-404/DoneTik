package com.datahiveorg.donetik.feature.onboarding.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.util.Animation.ANIMATION_DURATION_LONG


@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(pagerState.pageCount) { page ->
            val isSelected = (pagerState.currentPage == page)

            val width by animateDpAsState(
                targetValue = if (isSelected) 24.dp else 8.dp,
                animationSpec = tween(durationMillis = ANIMATION_DURATION_LONG)
            )
            val height by animateDpAsState(
                targetValue = 8.dp,
                animationSpec = tween(durationMillis = ANIMATION_DURATION_LONG)
            )
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) colorScheme.primary else Color.LightGray,
                animationSpec = tween(durationMillis = ANIMATION_DURATION_LONG)
            )

            val shape = if (isSelected) {
                RoundedCornerShape(percent = 50)
            } else {
                CircleShape
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp, vertical = 8.dp)
                    .width(width)
                    .height(height)
                    .clip(shape)
                    .background(backgroundColor)
            )
        }
    }
}
