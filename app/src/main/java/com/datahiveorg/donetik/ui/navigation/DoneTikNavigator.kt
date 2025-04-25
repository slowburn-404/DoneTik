package com.datahiveorg.donetik.ui.navigation

import androidx.navigation.NavHostController

interface DoneTikNavigator {
    fun navigate(screen: FeatureScreen)
}
