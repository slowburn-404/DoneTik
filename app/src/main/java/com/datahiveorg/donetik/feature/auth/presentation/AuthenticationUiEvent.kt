package com.datahiveorg.donetik.feature.auth.presentation

sealed interface AuthenticationUiEvent {
    data class ShowSnackBar(val message: String) : AuthenticationUiEvent
    data object None : AuthenticationUiEvent
    data object AuthenticationSuccessful : AuthenticationUiEvent
    sealed interface Navigate {
        data object Login : AuthenticationUiEvent
        data object SignUp : AuthenticationUiEvent
    }
}