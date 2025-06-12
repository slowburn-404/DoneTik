package com.datahiveorg.donetik.feature.auth.presentation

sealed interface AuthenticationIntent {
    data class EnterEmail(val email: String) : AuthenticationIntent
    data class EnterPassword(val password: String) : AuthenticationIntent
    data class EnterUsername(val username: String) : AuthenticationIntent
    data object Login : AuthenticationIntent
    data object SignUp : AuthenticationIntent
    data object ValidateForm: AuthenticationIntent
    data class SignInWithGoogle(val idToken: String): AuthenticationIntent
    data object UpdateUsername: AuthenticationIntent
}