package com.datahiveorg.donetik.feature.auth.data

import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.feature.auth.toDomain
import com.datahiveorg.donetik.feature.auth.toUserCredential
import com.datahiveorg.donetik.firebase.authentication.FirebaseAuthService
import com.datahiveorg.donetik.firebase.model.FirebaseResponse
import com.datahiveorg.donetik.util.DispatcherProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authService: FirebaseAuthService,
    private val dispatcher: DispatcherProvider,
) : AuthRepository {
    override suspend fun login(user: User): DomainResponse<User> {
        return withContext(dispatcher.io) {
            when (val response = authService.login(user.toUserCredential())) {
                is FirebaseResponse.Success -> {
                    DomainResponse.Success(response.data.toDomain())
                }

                is FirebaseResponse.Failure -> {
                    DomainResponse.Failure(
                        response.exception.toDomain()
                    )
                }
            }
        }
    }

    override suspend fun logout() {
        return withContext(dispatcher.io) {
            authService.logOut()
            //clear google auth credentials
        }
    }

    override suspend fun signUp(user: User): DomainResponse<User> {
        return withContext(dispatcher.io) {
            when (val response = authService.createAccount(user.toUserCredential())) {
                is FirebaseResponse.Success -> {
                    DomainResponse.Success(response.data.toDomain())
                }

                is FirebaseResponse.Failure -> {
                    DomainResponse.Failure(
                        response.exception.toDomain()
                    )
                }
            }
        }
    }

    override suspend fun signUpWithGoogle(idToken: String): DomainResponse<User> {
        return withContext(dispatcher.io) {
            when (val response = authService.signUpWithGoogle(idToken)) {
                is FirebaseResponse.Success -> {
                    DomainResponse.Success(response.data.toDomain())
                }

                is FirebaseResponse.Failure -> {
                    DomainResponse.Failure(response.exception.toDomain())
                }
            }
        }
    }

    override suspend fun getUser(): DomainResponse<User> {
        return withContext(dispatcher.io) {
            when (val response = authService.fetchUserInfo()) {
                is FirebaseResponse.Success -> {
                    DomainResponse.Success(response.data.toDomain())
                }

                is FirebaseResponse.Failure -> {
                    DomainResponse.Failure(
                        response.exception.toDomain()
                    )
                }
            }
        }
    }

    override suspend fun isUserLoggedIn(): DomainResponse<Boolean> {
        return withContext(dispatcher.io) {
            val response = authService.isLoggedIn.first()
            DomainResponse.Success(response)
        }
    }
}