package com.datahiveorg.donetik.feature.auth.domain.repository

import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User

interface AuthRepository {
    suspend fun login(user: User): DomainResponse<User>

    suspend fun logout()

    suspend fun signUp(user: User): DomainResponse<User>

    suspend fun signUpWithGoogle(idToken: String): DomainResponse<User>

    suspend fun getUser(): DomainResponse<User>

    suspend fun checkLoginStatus(): DomainResponse<Boolean>
}