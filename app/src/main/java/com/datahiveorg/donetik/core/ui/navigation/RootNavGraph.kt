package com.datahiveorg.donetik.core.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationNavigator
import com.datahiveorg.donetik.feature.auth.presentation.navigation.authenticationNavGraph
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import com.datahiveorg.donetik.feature.home.presentation.navigation.homeNavigationGraph
import com.datahiveorg.donetik.feature.leaderboard.presentation.navigation.LeaderBoardNavigator
import com.datahiveorg.donetik.feature.leaderboard.presentation.navigation.leaderBoardNavGraph
import com.datahiveorg.donetik.feature.onboarding.presentation.OnBoardingScreen
import com.datahiveorg.donetik.feature.onboarding.presentation.OnBoardingViewModel
import com.datahiveorg.donetik.feature.profile.presentation.navigation.ProfileNavigator
import com.datahiveorg.donetik.feature.profile.presentation.navigation.profileNavGraph
import com.datahiveorg.donetik.feature.router.RouterScreen
import com.datahiveorg.donetik.feature.router.RouterViewModel
import com.datahiveorg.donetik.feature.teams.presentation.navigation.TeamsNavigator
import com.datahiveorg.donetik.feature.teams.presentation.navigation.teamsNavGraph
import com.datahiveorg.donetik.util.GoogleSignHelper
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf

/**
 * Composable function that defines the root navigation graph of the application.
 *
 * This function sets up the main navigation structure using Jetpack Navigation Compose.
 * It includes navigation graphs for the application features.
 *
 * @param modifier Optional [Modifier] to be applied to the NavHost.
 * @param paddingValues [PaddingValues] to be applied as padding around the NavHost,
 * typically from a Scaffold.
 * @param snackBarHostState The [SnackbarHostState] used to show snackbars across different screens.
 * @param donetikNavigator The [DoneTikNavigator] instance responsible for handling app-wide navigation actions.
 * @param navController The [NavHostController] that manages the navigation within this graph.
 */
@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    snackBarHostState: SnackbarHostState,
    donetikNavigator: DoneTikNavigator,
    navController: NavHostController,
    googleSignInHelper: GoogleSignHelper
) {
    val authNavigator = getKoin().get<AuthenticationNavigator> { parametersOf(donetikNavigator) }
    val homeNavigator = getKoin().get<HomeNavigator> { parametersOf(donetikNavigator) }
    val leaderBoardNavigator =
        getKoin().get<LeaderBoardNavigator> { parametersOf(donetikNavigator) }
    val profileNavigator = getKoin().get<ProfileNavigator> { parametersOf(donetikNavigator) }
    val teamsNavigator = getKoin().get<TeamsNavigator> { parametersOf(donetikNavigator) }

    NavHost(
        modifier = modifier
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
        navController = navController,
        startDestination = RouterScreen
    ) {


        authenticationNavGraph(
            authenticationNavigator = authNavigator,
            snackBarHostState = snackBarHostState,
            googleSignHelper = googleSignInHelper
        )

        homeNavigationGraph(
            homeNavigator = homeNavigator,
            snackBarHostState = snackBarHostState,
        )

        animatedComposable<RouterScreen> {
            RouterScreen(
                viewModel = koinViewModel<RouterViewModel>(),
                onNavigate = { screen ->
                    donetikNavigator.navigate(screen)
                }
            )
        }

        animatedComposable<OnBoardingFeature> {
            OnBoardingScreen(
                doneTikNavigator = donetikNavigator,
                viewModel = koinViewModel<OnBoardingViewModel>()
            )
        }

        leaderBoardNavGraph(
            leaderBoardNavigator = leaderBoardNavigator,
            snackBarHostState = snackBarHostState
        )

        profileNavGraph(
            profileNavigator = profileNavigator,
            snackBarHostState = snackBarHostState
        )

        teamsNavGraph(
            teamsNavigator = teamsNavigator,
            snackBarHostState = snackBarHostState
        )
    }
}