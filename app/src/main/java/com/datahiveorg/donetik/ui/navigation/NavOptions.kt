package com.datahiveorg.donetik.ui.navigation

data class NavOptions(
    val inclusive: Boolean = true,
    val launchSingleTop: Boolean = true,
    val popUpToDestination: FeatureScreen? = null,
    val restoreState: Boolean = false,
    val saveState: Boolean = false
)
