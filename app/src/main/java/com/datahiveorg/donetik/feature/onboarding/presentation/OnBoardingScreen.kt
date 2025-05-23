package com.datahiveorg.donetik.feature.onboarding.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.ui.components.PrimaryButton
import com.datahiveorg.donetik.ui.navigation.AuthFeature
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.util.Animation.ANIMATION_DURATION_SHORT
import com.datahiveorg.donetik.util.Logger
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    state: OnBoardingState,
    onNavigate: (FeatureScreen) -> Unit,
    onEvent: (OnBoardingEvents) -> Unit
) {
    val context = LocalContext.current
    val pagerItems = PagerItems.entries
    val pagerState = rememberPagerState(pageCount = { pagerItems.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HorizontalPagerIndicator(
                pagerState = pagerState
            )
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = pagerState.pageCount - 1,
                            animationSpec = tween(ANIMATION_DURATION_SHORT)
                        )
                    }
                }

            ) {
                Text(
                    text = "SKIP",
                    style = typography.bodyMedium
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            beyondViewportPageCount = pagerState.pageCount
        ) { page ->
            val pagerItem = pagerItems[page]
            OnBoardingItem(
                pagerItem = pagerItem,
                context = context
            )
        }

        PrimaryButton(
            modifier = Modifier.padding(20.dp),
            label = if (pagerState.currentPage < 3) "Next" else "Get Started",
            onClick = {
                if (pagerState.currentPage < 3) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage + 1,
                        )
                    }
                } else {
                    onEvent(OnBoardingEvents.SetOnBoardingFinished(true))
                    Logger.i("hasFinishedOnBoarding", state.hasFinishedOnBoarding.toString())
                    onNavigate(AuthFeature)
                }
            },
            isLoading = false,
            isEnabled = true
        )

    }

}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun OnBoardingScreenPreview() {
//    MaterialTheme {
//        OnBoardingScreen(
//            modifier = Modifier,
//            onEvent = {},
//            onNavigate = {},
//
//            )
//    }
//}