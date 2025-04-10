package com.datahiveorg.donetik.router

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Stable
data class RouterState(
    val isLoggedIn: Boolean = false
)
