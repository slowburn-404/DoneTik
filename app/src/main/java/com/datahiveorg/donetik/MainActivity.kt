package com.datahiveorg.donetik

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.datahiveorg.donetik.core.ui.components.DoneTikScaffold
import com.datahiveorg.donetik.core.ui.navigation.AuthFeature
import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.core.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.core.ui.navigation.OnBoardingFeature
import com.datahiveorg.donetik.core.ui.navigation.RootNavGraph
import com.datahiveorg.donetik.core.ui.navigation.RouterScreen
import com.datahiveorg.donetik.core.ui.navigation.buildTopBarActions
import com.datahiveorg.donetik.core.ui.theme.DoneTikTheme
import com.datahiveorg.donetik.feature.auth.presentation.navigation.LoginScreen
import com.datahiveorg.donetik.feature.auth.presentation.navigation.SignUpScreen
import com.datahiveorg.donetik.feature.home.presentation.navigation.Feed
import com.datahiveorg.donetik.feature.home.presentation.navigation.NewTaskScreen
import com.datahiveorg.donetik.feature.home.presentation.navigation.TaskScreen
import com.datahiveorg.donetik.feature.leaderboard.presentation.navigation.LeaderBoard
import com.datahiveorg.donetik.feature.profile.presentation.navigation.Profile
import com.datahiveorg.donetik.feature.teams.presentation.navigation.Teams
import com.datahiveorg.donetik.util.GoogleSignHelper
import com.datahiveorg.donetik.util.Keys
import com.datahiveorg.donetik.util.Logger
import com.datahiveorg.donetik.util.signOut
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navigator = getKoin().get<DoneTikNavigator> { parametersOf(navController) }
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination
            val currentScreen =
                getCurrentScreen(destination = currentDestination)
            val viewModel: MainActivityViewModel = koinViewModel()
            val activityState by viewModel.state.collectAsStateWithLifecycle(MainActivityState())
            val googleSignHelper = remember { GoogleSignHelper(this@MainActivity) }
            val bottomNavigationScreens = listOf(
                Feed,
                LeaderBoard,
                Teams,
                Profile
            )
            val coroutineScope = rememberCoroutineScope()
            val profileImage = rememberAsyncImagePainter(
                model = ImageRequest
                    .Builder(this@MainActivity)
                    .data(
                        if (activityState.user.imageUrl == Uri.EMPTY) {
                            Keys.RANDOM_AVATAR_SERVICE_URL
                        } else {
                            activityState.user.imageUrl
                        }
                    )
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_profile),
                fallback = painterResource(R.drawable.ic_profile)
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
                DoneTikScaffold(
                    navigator = navigator,
                    currentScreen = currentScreen,
                    currentDestination = currentDestination,
                    snackBarHostState = activityState.snackBarHostState,
                    bottomNavigationScreens = bottomNavigationScreens,
                    content = { innerPadding ->
                        RootNavGraph(
                            donetikNavigator = navigator,
                            paddingValues = innerPadding,
                            snackBarHostState = activityState.snackBarHostState,
                            navController = navController,
                            googleSignInHelper = googleSignHelper,
                        )
                    },
                    topBarActions = currentScreen?.let { screen ->
                        buildTopBarActions(
                            featureScreen = screen,
                            onClick = {
                                Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
                                viewModel.signOut()
                                coroutineScope.launch {
                                    this@MainActivity.signOut(googleSignHelper)
                                }
                                navController.navigate(AuthFeature) {
                                    launchSingleTop = true
                                    popUpTo<Feed> {
                                        inclusive = true
                                    }
                                }
                            },
                            painter = profileImage
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
        LoginScreen::class.simpleName.orEmpty() in route -> LoginScreen
        SignUpScreen::class.simpleName.orEmpty() in route -> SignUpScreen
        OnBoardingFeature::class.simpleName.orEmpty() in route -> OnBoardingFeature
        RouterScreen::class.simpleName.orEmpty() in route -> RouterScreen
        Feed::class.simpleName.orEmpty() in route -> Feed
        TaskScreen::class.simpleName.orEmpty() in route -> TaskScreen("", "")
        NewTaskScreen::class.simpleName.orEmpty() in route -> NewTaskScreen
        LeaderBoard::class.simpleName.orEmpty() in route -> LeaderBoard
        Profile::class.simpleName.orEmpty() in route -> Profile
        Teams::class.simpleName.orEmpty() in route -> Teams
        else -> null
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoneTikTheme {}
}