package com.datahiveorg.donetik.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import com.datahiveorg.donetik.MainActivityState
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.ui.navigation.buildTopBarActions
import com.datahiveorg.donetik.ui.navigation.getFABDestination

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
    snackBarHostState: SnackbarHostState,
    navigator: DoneTikNavigator,
    currentScreen: FeatureScreen?,
    currentDestination: NavDestination?,
    activityState: MainActivityState,
    bottomBarScreens: List<FeatureScreen>
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                SnackBar(
                    message = it.visuals.message
                )
            }
        },
        topBar = {
            currentScreen?.let { screen ->
                if (screen.screenUIConfig.hasTopAppBar) {
                    TopBar(
                        showNavigationIcon = screen.screenUIConfig.hasNavIcon,
                        onBackClick = {
                            navigator.navigateUp()
                        },
                        actions = activityState.user?.imageUrl?.let {
                            buildTopBarActions(
                                screen,
                                it,
                                onClick = {
                                    //TODO(Show dialog for logging out and such)
                                }
                            )
                        },
                        title = screen.screenUIConfig.title
                    )
                }
            }
        },
        bottomBar = {
            currentScreen?.let { screen ->
                AnimatedBottomNavBar(
                    navigator = navigator,
                    currentDestination = currentDestination,
                    isVisible = screen.screenUIConfig.hasBottomBar,
                    bottomBarScreens = bottomBarScreens
                )
            }

        },
        floatingActionButton = {
            currentScreen?.let { screen ->
                val destination = screen.getFABDestination()
                AnimatedFAB(
                    isVisible = screen.screenUIConfig.hasFAB,
                    onClick = { navigator.navigate(destination) }
                )
            }
        },
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        content = content
    )

}