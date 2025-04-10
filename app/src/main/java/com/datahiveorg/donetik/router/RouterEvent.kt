package com.datahiveorg.donetik.router

import com.datahiveorg.donetik.ui.navigation.FeatureScreen

sealed interface RouterEvent {
    data class Navigate(val screen: FeatureScreen): RouterEvent
    data object None: RouterEvent
}