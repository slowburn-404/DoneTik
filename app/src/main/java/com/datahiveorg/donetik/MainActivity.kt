package com.datahiveorg.donetik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationScreen
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen
import com.datahiveorg.donetik.ui.components.AnimatedBottomNavBar
import com.datahiveorg.donetik.ui.components.AnimatedFAB
import com.datahiveorg.donetik.ui.components.SnackBar
import com.datahiveorg.donetik.ui.components.TopBar
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.ui.navigation.OnBoardingFeature
import com.datahiveorg.donetik.ui.navigation.RootNavGraph
import com.datahiveorg.donetik.ui.navigation.RouterScreen
import com.datahiveorg.donetik.ui.navigation.getFABDestination
import com.datahiveorg.donetik.ui.theme.DoneTikTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination
            val currentScreen =
                getCurrentScreen(destination = currentDestination)
            val snackBarHostState = SnackbarHostState()

            DoneTikTheme {
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
                        currentScreen?.takeIf { featureScreen ->
                            featureScreen.hasTopAppBar
                        }?.let { screen ->
                            TopBar(
                                showNavigationIcon = screen.hasNavIcon,
                                onBackClick = {
                                    navController.navigateUp()
                                },
                                actions = screen.topBarActions,
                                title = screen.title
                            )
                        }
                    },
                    bottomBar = {
                        currentScreen?.takeIf { featureScreen ->
                            featureScreen.hasBottomBar
                        }?.let {
                            AnimatedBottomNavBar(
                                navController = navController,
                                currentDestination = currentDestination
                            )
                        }

                    },
                    floatingActionButton = {
                        currentScreen?.takeIf { featureScreen ->
                            featureScreen.hasFAB
                        }?.let { screen ->
                            val route = screen.getFABDestination()
                            AnimatedFAB(
                                isVisible = true,
                                onClick = { navController.navigate(route) }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                ) { innerPadding ->
                    RootNavGraph(
                        paddingValues = innerPadding,
                        snackBarHostState = snackBarHostState,
                        navController = navController,
                    )
                }
            }
        }
    }
}

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