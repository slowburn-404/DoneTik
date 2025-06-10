package com.datahiveorg.donetik.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.core.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.core.ui.navigation.TopBarAction
import com.datahiveorg.donetik.core.ui.navigation.getFABDestination
import com.datahiveorg.donetik.util.Logger

/**
 * Composable function that provides a scaffold for the app, including top bar, bottom bar,
 * floating action button, and snack bar.
 *
 * @param modifier The modifier to be applied to the scaffold.
 * @param content The content to be displayed in the scaffold.
 * @param snackBarHostState The state of the snack bar.
 * @param navigator The navigator used for navigating between screens.
 * @param currentScreen The current screen being displayed.
 * @param currentDestination The current destination in the navigation graph.
 * @param bottomNavigationScreens The list of screens to be displayed in the bottom bar.
 */
@Composable
fun DoneTikScaffold(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
    snackBarHostState: SnackbarHostState,
    navigator: DoneTikNavigator,
    currentScreen: FeatureScreen?,
    currentDestination: NavDestination?,
    bottomNavigationScreens: List<FeatureScreen>,
    topBarActions: List<TopBarAction>?
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
                Logger.i("Screen State", screen.screenUIConfig.toString())
                if (screen.screenUIConfig.hasTopAppBar) {
                    TopBar(
                        showNavigationIcon = screen.screenUIConfig.hasNavIcon,
                        navigationIcon = screen.screenUIConfig.navIconRes,
                        onBackClick = {
                            navigator.navigateUp()
                        },
                        title = screen.screenUIConfig.title,
                        actions = topBarActions,
                        enterAnimationTransition = screen.screenUIConfig.enterTransition,
                        exitAnimationTransition = screen.screenUIConfig.exitTransition
                    )
                }
            }
        },
        bottomBar = {
            currentScreen?.let { screen ->
                Logger.i("Screen State", screen.screenUIConfig.toString())
                AnimatedBottomNavBar(
                    navigator = navigator,
                    currentDestination = currentDestination,
                    isVisible = screen.screenUIConfig.hasBottomBar,
                    bottomNavigationScreens = bottomNavigationScreens
                )
            }

        },
        floatingActionButton = {
            currentScreen?.let { screen ->
                val destination = screen.getFABDestination()
                AnimatedFAB(
                    isVisible = screen.screenUIConfig.hasFAB,
                    onClick = {
                        navigator.navigate(destination)
                    }
                )
            }
        },
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        content = content
    )

}