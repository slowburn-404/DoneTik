package com.datahiveorg.donetik.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.datahiveorg.donetik.ui.navigation.TopBarAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    actions: List<TopBarAction>?,
) {
    MediumTopAppBar(
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        title = {},
        actions = {
            actions?.let{
                it.forEach { action ->
                    IconButton(
                        onClick = action.onClick
                    ) {
                        Icon(
                            painter = painterResource(id = action.iconRes),
                            contentDescription = action.description,
                        )
                    }
                }
            }
        },
    )

}