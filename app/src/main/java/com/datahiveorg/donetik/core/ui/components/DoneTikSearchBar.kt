package com.datahiveorg.donetik.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.feature.home.presentation.feed.TaskCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoneTikSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onSearch: (String) -> Unit,
    searchResults: List<Task>,
    onQueryChange: (String) -> Unit,
    isExpanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    leadingIcon: @Composable () -> Unit,
    onSearchResultClick: (Task) -> Unit,
    placeholder: String = "Search"
) {
    SearchBar(
        modifier = modifier
            .semantics { traversalIndex = 0f },
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                expanded = isExpanded,
                onExpandedChange = { onExpandedChanged(it) },
                leadingIcon = leadingIcon,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        },
        expanded = isExpanded,
        onExpandedChange = { onExpandedChanged(it) },
        shape = shapes.extraLarge,
        colors = SearchBarDefaults.colors(
            containerColor = colorScheme.surfaceContainer,
        )
    ) {
        if (searchResults.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = searchResults,
                    key = { task -> task.id }

                ) {
                    TaskCard(
                        task = it,
                        onClick = { onSearchResultClick(it) },
                        onLongClick = {}
                    )
                }
            }
        }
    }

}