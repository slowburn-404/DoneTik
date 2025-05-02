package com.datahiveorg.donetik.feature.auth.presentation

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.datahiveorg.donetik.feature.auth.domain.model.User

@Stable
@Immutable
data class AuthenticationUiState(
    val user: User = User(
        email = "",
        password = "",
        imageUrl = Uri.EMPTY,
        uid = "",
        username = ""
    ),
    val isLoading: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val emailError: String = "",
    val passwordError: String = "",
    val isPasswordVisible: Boolean = false,
    val isFormValid: Boolean = false,
    val isAuthenticated: Boolean = false,
)
