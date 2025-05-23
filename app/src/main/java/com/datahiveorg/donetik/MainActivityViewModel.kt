package com.datahiveorg.donetik

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    init {
       getUserInfo()
    }

    private val _state = MutableStateFlow(MainActivityState())
    val state = _state.asStateFlow()

    private fun getUserInfo() {
        viewModelScope.launch {
            val response = authRepository.getUser()
            if (response is DomainResponse.Success) {
                _state.value = _state.value.copy(
                    user = response.data
                )
            }

        }
    }

}

data class MainActivityState(
    val user: User? = null,
    val snackBarHostState: SnackbarHostState = SnackbarHostState()
)