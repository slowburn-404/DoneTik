package com.datahiveorg.donetik.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.datahiveorg.donetik.ui.navigation.TopBarAction

/**
 * A composable function that displays a top app bar.
 *
 * @param modifier The modifier to be applied to the top app bar.
 * @param showNavigationIcon Whether to show the navigation icon.
 * @param navigationIcon The drawable resource ID for the navigation icon.
 * @param onBackClick The callback to be invoked when the navigation icon is clicked.
 * @param actions The list of actions to be displayed in the top app bar.
 * @param title The title to be displayed in the top app bar.
 */
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
    TopAppBar(
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
                    action.icon()

                }
            }
        },
    )

}