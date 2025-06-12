package com.datahiveorg.donetik.feature.auth.presentation

sealed interface AuthenticationUiEvent {
    data class ShowSnackBar(val message: String) : AuthenticationUiEvent
    sealed interface Navigate {
        data object Login : AuthenticationUiEvent
        data object SignUp : AuthenticationUiEvent
        data object UpdateUsername: AuthenticationUiEvent
        data object Home : AuthenticationUiEvent

    }
}