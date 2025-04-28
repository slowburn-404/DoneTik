package com.datahiveorg.donetik.feature.home.presentation.feed

import com.datahiveorg.donetik.ui.navigation.FeatureScreen

interface FeedEvent {
    data class ShowSnackBar(val message: String) : FeedEvent
    data class Navigate(val screen: FeatureScreen) : FeedEvent
    data class SelectTask(val taskId: String, val userId: String) : FeedEvent
}