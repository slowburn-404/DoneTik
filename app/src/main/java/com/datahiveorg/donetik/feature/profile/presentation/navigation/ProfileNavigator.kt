package com.datahiveorg.donetik.feature.profile.presentation.navigation

import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator

interface ProfileNavigator {
    fun navigateToProfile ()
}

class ProfileNavigatorImpl(
    private val doneTikNavigator: DoneTikNavigator
): ProfileNavigator {
    override fun navigateToProfile() {
       doneTikNavigator.navigate(Profile)
    }
}