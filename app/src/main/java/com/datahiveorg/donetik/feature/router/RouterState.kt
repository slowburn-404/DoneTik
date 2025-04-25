package com.datahiveorg.donetik.feature.router

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.datahiveorg.donetik.feature.auth.domain.model.User

@Immutable
@Stable
data class RouterState(
    val isLoggedIn: Boolean = false,
    val hasFinishedOnBoarding: Boolean = false,
    val user: User = User(
        "",
        "",
        "",
        Uri.EMPTY,
        "",
    )
)
