package com.datahiveorg.donetik.feature.auth.presentation

sealed interface AuthenticationUiEvent {
    data object None : AuthenticationUiEvent
    data class ShowSnackBar(val message: String) : AuthenticationUiEvent
    sealed interface Navigate {
        data object Login : AuthenticationUiEvent
        data object SignUp : AuthenticationUiEvent
    }
}