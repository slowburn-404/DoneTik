package com.datahiveorg.donetik.feature.auth.domain.repository

import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User

/**
 * Repository interface for authentication-related operations.
 *
 * This interface defines the contract for interacting with the authentication data source,
 * whether it's a remote server, local database, or any other storage mechanism.
 * It provides methods for user login, logout, sign-up, and retrieving user information.
 */
interface AuthRepository {
    suspend fun login(user: User): DomainResponse<User>

    suspend fun logout()

    suspend fun signUp(user: User): DomainResponse<User>

    suspend fun signUpWithGoogle(idToken: String): DomainResponse<User>

    suspend fun getUser(): DomainResponse<User?>

    suspend fun checkLoginStatus(): DomainResponse<Boolean>
}