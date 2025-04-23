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
import com.datahiveorg.donetik.ui.components.BottomNavBar
import com.datahiveorg.donetik.ui.components.SnackBar
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.ui.navigation.OnBoardingFeature
import com.datahiveorg.donetik.ui.navigation.RootNavGraph
import com.datahiveorg.donetik.ui.navigation.RouterScreen
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
                    bottomBar = {
                        currentScreen?.takeIf { it.hasBottomBar }?.let {
                            BottomNavBar(
                                navController = navController,
                                currentDestination = currentDestination
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
                        navController = navController
                    )
                }
            }
        }
    }
}

private fun getCurrentScreen(
    destination: NavDestination?,
): FeatureScreen? {
    return when (destination?.route) {
        AuthenticationScreen.LoginScreen.route -> AuthenticationScreen.LoginScreen
        AuthenticationScreen.SignUpScreen.route -> AuthenticationScreen.SignUpScreen
        OnBoardingFeature.route -> OnBoardingFeature
        RouterScreen.route -> RouterScreen
        HomeScreen.Feed("").route -> HomeScreen.Feed("")
        HomeScreen.TaskScreen("", "").route -> HomeScreen.TaskScreen("", "")
        HomeScreen.NewTaskScreen.route -> HomeScreen.NewTaskScreen
        else -> null
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoneTikTheme {
    }
}