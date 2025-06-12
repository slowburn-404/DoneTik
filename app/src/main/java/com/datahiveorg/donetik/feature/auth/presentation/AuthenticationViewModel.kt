package com.datahiveorg.donetik.feature.auth.presentation

import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AuthenticationUiState())
    val state: StateFlow<AuthenticationUiState> = _state.asStateFlow()

    private val _uiEvents = Channel<AuthenticationUiEvent>(Channel.BUFFERED)
    val uiEvents = _uiEvents.receiveAsFlow()

    private val _uiIntents: MutableSharedFlow<AuthenticationIntent> =
        MutableSharedFlow(extraBufferCapacity = 64)
    private val uiIntents: SharedFlow<AuthenticationIntent> = _uiIntents.asSharedFlow()

    init {
        viewModelScope.launch {
            uiIntents.collectLatest { intent ->
                when (intent) {
                    is AuthenticationIntent.EnterEmail -> enterEmail(intent.email)
                    is AuthenticationIntent.EnterPassword -> enterPassword(intent.password)
                    is AuthenticationIntent.SignUp -> signUp()
                    is AuthenticationIntent.Login -> login()
                    is AuthenticationIntent.ValidateForm -> validateAndUpdateFormState()
                    is AuthenticationIntent.SignInWithGoogle -> signInWithGoogle(intent.idToken)
                    is AuthenticationIntent.EnterUsername -> enterUsername(intent.username)
                    is AuthenticationIntent.UpdateUsername -> updateUsername()
                }
            }
        }
    }

    fun emitEvent(event: AuthenticationUiEvent) {
        viewModelScope.launch {
            _uiEvents.send(event)
        }
    }

    fun emitIntent(intent: AuthenticationIntent) {
        viewModelScope.launch {
            _uiIntents.emit(intent)
        }
    }

    private fun enterEmail(email: String) {
        val isValid = validateEmail(email)
        _state.update { currentState ->
            currentState.copy(
                user = currentState.user.copy(email = email),
                isEmailValid = isValid,
                emailError = if (!isValid) "Enter a valid email address" else ""
            )
        }

        validateAndUpdateFormState()
    }

    private fun enterPassword(password: String) {
        val passwordError = validatePassword(password)
        _state.update { currentState ->
            currentState.copy(
                user = currentState.user.copy(password = password),
                isPasswordValid = passwordError.isEmpty(),
                passwordError = passwordError
            )
        }

        validateAndUpdateFormState()
    }

    private fun validateEmail(email: String): Boolean =
        email.isNotEmpty() && EMAIL_ADDRESS.matcher(email).matches()

    private fun validatePassword(password: String): String {
        val regexPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%^&*]).*\$"
        if (password.length < 6) return "Password must be least 6 characters long"

        return if (!password.matches(Regex(regexPattern))) {
            "Password must contain at least one digit," +
                    " one uppercase letter, " +
                    "one lowercase letter," +
                    " and one special character from !@#\$%^&*"
        } else {
            ""
        }
    }

    private fun showLoadingIndicator() {
        _state.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
    }

    private suspend fun login() {
        val user = _state.value.user
        showLoadingIndicator()
        when (val response = authRepository.login(user)) {
            is DomainResponse.Failure -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isAuthenticated = false
                    )
                }
                emitEvent(AuthenticationUiEvent.ShowSnackBar(response.message))
            }

            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                }

                emitEvent(AuthenticationUiEvent.Navigate.Home)
            }
        }
        validateAndUpdateFormState()
    }

    private suspend fun signUp() {
        val user = _state.value.user
        showLoadingIndicator()
        when (val response = authRepository.signUp(user)) {
            is DomainResponse.Failure -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isAuthenticated = false
                    )
                }
                emitEvent(AuthenticationUiEvent.ShowSnackBar(response.message))
            }

            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                }

                emitEvent(AuthenticationUiEvent.Navigate.UpdateUsername)
            }
        }
        validateAndUpdateFormState()
    }

    private fun validateForm(): Boolean {
        val state = _state.value
        return state.isPasswordValid &&
                state.isEmailValid &&
                state.user.email.isNotEmpty() &&
                state.user.password.isNotEmpty() &&
                state.emailError.isEmpty() &&
                state.passwordError.isEmpty() &&
                !state.isLoading
    }

    private fun validateAndUpdateFormState() {
        val isValid = validateForm()
        _state.update { currentState ->
            currentState.copy(isFormValid = isValid)
        }
    }

    private suspend fun signInWithGoogle(idToken: String) {
        showLoadingIndicator()
        when (val response = authRepository.signUpWithGoogle(idToken)) {
            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        user = response.data,
                        isLoading = false,
                        isFormValid = true,
                        isAuthenticated = true,
                    )
                }
                emitEvent(AuthenticationUiEvent.Navigate.Home)
            }

            is DomainResponse.Failure -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isAuthenticated = false
                    )
                }
                emitEvent(AuthenticationUiEvent.ShowSnackBar(response.message))
            }
        }
    }

    private fun enterUsername(username: String) {
        _state.update { currentState ->
            currentState.copy(
                user = currentState.user.copy(username = username)
            )
        }
    }

    private suspend fun updateUsername() {
        val username = _state.value.user.username
        showLoadingIndicator()
        when (val response = authRepository.updateUsername(username)) {
            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                    )
                }
                emitEvent(AuthenticationUiEvent.Navigate.Home)
            }

            is DomainResponse.Failure -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                    )
                }
                emitEvent(AuthenticationUiEvent.ShowSnackBar(response.message))
            }
        }
    }
}