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
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.ui.navigation.getFABDestination
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
 * @param bottomBarScreens The list of screens to be displayed in the bottom bar.
 */
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
    snackBarHostState: SnackbarHostState,
    navigator: DoneTikNavigator,
    currentScreen: FeatureScreen?,
    currentDestination: NavDestination?,
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
                Logger.i("Screen State", screen.screenUIConfig.title)
                if (screen.screenUIConfig.hasTopAppBar) {
                    TopBar(
                        showNavigationIcon = screen.screenUIConfig.hasNavIcon,
                        navigationIcon = screen.screenUIConfig.navIconRes,
                        onBackClick = {
                            navigator.navigateUp()
                        },
                        title = screen.screenUIConfig.title,
                        actions = null,
                        enterAnimationTransition = screen.screenUIConfig.enterTransition,
                        exitAnimationTransition = screen.screenUIConfig.exitTransition
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