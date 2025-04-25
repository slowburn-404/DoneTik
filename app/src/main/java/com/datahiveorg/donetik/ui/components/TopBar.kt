package com.datahiveorg.donetik.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
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
    showNavigationIcon: Boolean = false,
    @DrawableRes navigationIcon: Int? = null,
    onBackClick: () -> Unit,
    actions: List<TopBarAction>?,
    title: String
) {
    MediumTopAppBar(
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            if (showNavigationIcon) {
                IconButton(
                    onClick = onBackClick
                ) {
                    navigationIcon?.let { id ->
                        Icon(
                            painter = painterResource(id),
                            contentDescription = "Back",
                        )
                    }
                }
            }
        },
        title = {
            ScreenTitle(
                title = title
            )

        },
        actions = {
            actions?.let {
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