package com.datahiveorg.donetik.feature.router

import com.datahiveorg.donetik.core.ui.navigation.FeatureScreen

sealed interface RouterEvent {
    data class Navigate(val screen: FeatureScreen) : RouterEvent
}