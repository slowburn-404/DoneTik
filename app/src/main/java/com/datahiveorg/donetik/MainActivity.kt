package com.datahiveorg.donetik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationScreen
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen
import com.datahiveorg.donetik.ui.components.AppScaffold
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.ui.navigation.OnBoardingFeature
import com.datahiveorg.donetik.ui.navigation.RootNavGraph
import com.datahiveorg.donetik.ui.navigation.RouterScreen
import com.datahiveorg.donetik.ui.theme.DoneTikTheme
import com.datahiveorg.donetik.util.Logger
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            )
        )
        setContent {
            val navController = rememberNavController()
            val navigator = getKoin().get<DoneTikNavigator> { parametersOf(navController) }
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination
            val currentScreen =
                getCurrentScreen(destination = currentDestination)
            val viewModel: MainActivityViewModel = koinViewModel()
            val activityState by viewModel.state.collectAsStateWithLifecycle()
            val bottomBarScreens = listOf(
                HomeScreen.Feed
            )

            DisposableEffect(navController) {
                val listener =
                    NavController.OnDestinationChangedListener { controller, destination, arguments ->
                        Logger.i("RootNavigation", "Destination changed to: ${destination.route}")
                        Logger.i("RootNavigation", "Arguments: $arguments")
                        val backStack = controller.currentBackStackEntry
                        Logger.i("Back stack entry", "$backStack")


                    }
                navController.addOnDestinationChangedListener(listener)
                onDispose {
                    navController.removeOnDestinationChangedListener(listener)
                }
            }

            DoneTikTheme {
                AppScaffold(
                    navigator = navigator,
                    currentScreen = currentScreen,
                    currentDestination = currentDestination,
                    snackBarHostState = activityState.snackBarHostState,
                    activityState = activityState,
                    bottomBarScreens = bottomBarScreens,
                    content = { innerPadding ->
                        RootNavGraph(
                            navigator = navigator,
                            paddingValues = innerPadding,
                            snackBarHostState = activityState.snackBarHostState,
                            navController = navController,
                        )
                    }
                )

            }
        }
    }
}

//will fail when obfuscation is done
private fun getCurrentScreen(
    destination: NavDestination?,
): FeatureScreen? {
    if (destination == null) return null

    val route = destination.route ?: return null

    return when {
        AuthenticationScreen.LoginScreen::class.simpleName.orEmpty() in route -> AuthenticationScreen.LoginScreen
        AuthenticationScreen.SignUpScreen::class.simpleName.orEmpty() in route -> AuthenticationScreen.SignUpScreen
        OnBoardingFeature::class.simpleName.orEmpty() in route -> OnBoardingFeature
        RouterScreen::class.simpleName.orEmpty() in route -> RouterScreen
        HomeScreen.Feed::class.simpleName.orEmpty() in route -> HomeScreen.Feed
        HomeScreen.TaskScreen::class.simpleName.orEmpty() in route -> HomeScreen.TaskScreen("", "")
        HomeScreen.NewTaskScreen::class.simpleName.orEmpty() in route -> HomeScreen.NewTaskScreen
        else -> null
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoneTikTheme {}
}