package com.datahiveorg.donetik.feature.auth.data

import android.net.Uri
import com.datahiveorg.donetik.core.firebase.authentication.AuthDataSource
import com.datahiveorg.donetik.core.firebase.storage.StorageDataSource
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.util.DispatcherProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val dispatcher: DispatcherProvider,
    private val storageDataSource: StorageDataSource
) : AuthRepository {
    override suspend fun login(user: User): DomainResponse<User> {
        return withContext(dispatcher.io) {
            val response = authDataSource.login(user.toUserCredential())
            return@withContext response.fold(
                onSuccess = { user ->
                    if (user != null) {
                        DomainResponse.Success(user.toAuthDomain())
                    } else {
                        DomainResponse.Error("User not found")
                    }
                },
                onFailure = { exception ->
                    DomainResponse.Error(exception.toAuthDomain())
                }
            )
        }
    }

    override suspend fun logout() {
        return withContext(dispatcher.io) {
            authDataSource.logOut()
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
                        DomainResponse.Error("User not found")
                    }
                },
                onFailure = { exception ->
                    DomainResponse.Error(exception.toAuthDomain())
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
                        DomainResponse.Error("User not found")
                    } //😏
                },
                onFailure = { exception ->
                    DomainResponse.Error(exception.toAuthDomain())
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
                        DomainResponse.Error("User not found")
                    }
                },
                onFailure = { exception ->
                    DomainResponse.Error(exception.toAuthDomain())

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

    override suspend fun updateUsername(userName: String): DomainResponse<Unit> {
        return withContext(dispatcher.io) {
            authDataSource
                .updateUsername(userName)
                .fold(
                    onSuccess = {
                        DomainResponse.Success(Unit)
                    },
                    onFailure = {
                        DomainResponse.Error(it.toAuthDomain())
                    }
                )
        }
    }

    override suspend fun updateProfilePicture(newImage: Uri): DomainResponse<Unit> {
        return withContext(dispatcher.io) {
            authDataSource
                .updateProfilePicture(newImage = newImage)
                .fold(
                    onSuccess = {
                        DomainResponse.Success(Unit)
                    },
                    onFailure = { exception ->
                        DomainResponse.Error(exception.toAuthDomain())
                    }
                )
        }
    }

    override suspend fun uploadProfileImage(newImage: Uri, userId: String): DomainResponse<Uri> {
        return withContext(dispatcher.io) {
            storageDataSource
                .uploadImage(newImage, userId)
                .fold(
                    onSuccess = { uri ->
                        DomainResponse.Success(uri)
                    },
                    onFailure = {
                        DomainResponse.Error(it.toAuthDomain())
                    }
                )
        }
    }
}