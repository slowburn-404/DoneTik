package com.datahiveorg.donetik.feature.profile.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.datahiveorg.donetik.core.ui.navigation.ProfileFeature
import com.datahiveorg.donetik.core.ui.navigation.animatedComposable
import com.datahiveorg.donetik.feature.profile.presentation.ProfileScreen

fun NavGraphBuilder.profileNavGraph(
    profileNavigator: ProfileNavigator,
    snackBarHostState: SnackbarHostState

) {

    navigation<ProfileFeature>(
        startDestination = Profile
    ) {
        animatedComposable<Profile> {
            ProfileScreen(
                profileNavigator = profileNavigator,
                snackBarHostState = snackBarHostState
            )
        }

    }

}