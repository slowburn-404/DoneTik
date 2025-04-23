package com.datahiveorg.donetik.feature.home.presentation.feed

interface FeedEvent {
    data class ShowSnackBar(val message: String) : FeedEvent
}