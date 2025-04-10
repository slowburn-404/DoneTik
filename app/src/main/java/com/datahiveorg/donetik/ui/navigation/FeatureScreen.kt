package com.datahiveorg.donetik.ui.navigation

interface FeatureScreen {
    val route: String
    val title: String
}

data object RouterScreen: FeatureScreen {
    override val route: String
        get() = "router_screen"

    override val title: String
        get() = ""
}

data object AuthFeature: FeatureScreen {
    override val title: String
        get() = ""
    override val route: String
        get() = "auth"
}