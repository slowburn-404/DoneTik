package com.datahiveorg.donetik

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainActivityState()
    ).onStart {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            val response = authRepository.getUser()
            if (response is DomainResponse.Success) {
                _state.value = _state.value.copy(
                    user = response.data ?: MainActivityState().user
                )
            }

        }
    }

    //TODO: improve error handling
    fun signOut() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

}

data class MainActivityState(
    val user: User = User(
        uid = "",
        username = "",
        email = "",
        imageUrl = Uri.EMPTY,
        password = ""
    ),
    val snackBarHostState: SnackbarHostState = SnackbarHostState()
)