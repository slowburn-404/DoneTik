package com.datahiveorg.donetik.feature.home.presentation.feed

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.core.ui.components.AnimatedText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsCarousel(
    modifier: Modifier = Modifier,
    carouselItems: Set<CarouselItem>,
    carouselState: CarouselState
) {
    HorizontalMultiBrowseCarousel(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        state = carouselState,
        itemSpacing = 8.dp,
        preferredItemWidth = 250.dp,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) { i ->
        if (carouselItems.isEmpty() ) return@HorizontalMultiBrowseCarousel
        val item = carouselItems.elementAt(i)
        StatsCarouselItem(
            modifier = Modifier.maskClip(shapes.extraLarge),
            carouselItem = item
        )
    }


}

@Composable
fun StatsCarouselItem(
    modifier: Modifier = Modifier,
    carouselItem: CarouselItem
) {

    Card(
        modifier = modifier
            .height(205.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.primaryContainer,
            contentColor = colorScheme.onPrimaryContainer
        ),
        shape = shapes.extraLarge,
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = carouselItem.category,
                    style = typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
                AnimatedText(
                    text = pluralStringResource(
                        id = R.plurals.number_of_tasks,
                        count = carouselItem.count,
                        carouselItem.count
                    ),
                    style = typography.titleMedium,
                    color = colorScheme.onPrimaryContainer,
                    transitionSpec = {
                        slideInVertically { it } + fadeIn() togetherWith
                                slideOutVertically { -it } + fadeOut()
                    }
                )
            }
        }

    }
}


data class CarouselItem(
    val category: String,
    val count: Int,
    val contentDescription: String
)