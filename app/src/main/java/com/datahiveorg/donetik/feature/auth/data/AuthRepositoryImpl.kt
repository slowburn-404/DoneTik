package com.datahiveorg.donetik.feature.auth.data

import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.firebase.authentication.AuthDataSource
import com.datahiveorg.donetik.util.DispatcherProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val dispatcher: DispatcherProvider,
) : AuthRepository {
    override suspend fun login(user: User): DomainResponse<User> {
        return withContext(dispatcher.io) {
            val response = authDataSource.login(user.toUserCredential())
            return@withContext response.fold(
                onSuccess = { user ->
                    if (user != null) {
                        DomainResponse.Success(user.toAuthDomain())
                    } else {
                        DomainResponse.Failure("User not found")
                    }
                },
                onFailure = { exception ->
                    DomainResponse.Failure(exception.toAuthDomain())
                }
            )
        }
    }

    override suspend fun logout() {
        return withContext(dispatcher.io) {
            authDataSource.logOut()
            //clear google auth credentials
        }
    }

    override suspend fun signUp(user: User): DomainResponse<User> {
        return withContext(dispatcher.io) {
            val response = authDataSource.createAccount(user.toUserCredential())
            return@withContext response.fold(
                onSuccess = { user ->
                    if (user != null) {
                        DomainResponse.Success(user.toAuthDomain())
                    } else {
                        DomainResponse.Failure("User not found")
                    }
                },
                onFailure = { exception ->
                    DomainResponse.Failure(exception.toAuthDomain())
                }
            )
        }
    }

    override suspend fun signUpWithGoogle(idToken: String): DomainResponse<User> {
        return withContext(dispatcher.io) {
            val response = authDataSource.signUpWithGoogle(idToken)
            return@withContext response.fold(
                onSuccess = { user ->
                    user?.let {
                        DomainResponse.Success(it.toAuthDomain())
                    } ?: run {
                        DomainResponse.Failure("User not found")
                    } //😏
                },
                onFailure = { exception ->
                    DomainResponse.Failure(exception.toAuthDomain())
                }
            )

        }
    }

    override suspend fun getUser(): DomainResponse<User?> {
        return withContext(dispatcher.io) {
            val response = authDataSource.fetchUserInfo()
            return@withContext response.fold(
                onSuccess = { user ->
                    user?.let {
                        DomainResponse.Success(it.toAuthDomain())
                    } ?: run {
                        DomainResponse.Failure("User not found")
                    }
                },
                onFailure = { exception ->
                    DomainResponse.Failure(exception.toAuthDomain())

                }
            )
        }
    }

    override suspend fun checkLoginStatus(): DomainResponse<Boolean> {
        return withContext(dispatcher.io) {
            val response = authDataSource.isLoggedIn.first()
            DomainResponse.Success(response)
        }
    }
}