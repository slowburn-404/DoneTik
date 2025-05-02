package com.datahiveorg.donetik.ui.navigation

data class NavOptions(
    val inclusive: Boolean = false,
    val launchSingleTop: Boolean = false,
    val popUpToDestination: FeatureScreen? = null,
    val restoreState: Boolean = true,
    val saveState: Boolean = true
)
