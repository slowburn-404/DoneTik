package com.datahiveorg.donetik.feature.home.presentation.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoriesRow(
    modifier: Modifier = Modifier,
    categories: List<String>,
    lazyListState: LazyListState,
    selectedCategory: String,
    onCategoryClick: (String) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = lazyListState,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = categories,
            key = { it.hashCode() }
        ) { category ->
            FilterChip(
                modifier = Modifier.animateItem(),
                selected = selectedCategory == category,
                onClick = { onCategoryClick(category) },
                label = {
                    Text(
                        text = category,
                        style = typography.labelLarge
                    )
                }
            )
        }
    }
}