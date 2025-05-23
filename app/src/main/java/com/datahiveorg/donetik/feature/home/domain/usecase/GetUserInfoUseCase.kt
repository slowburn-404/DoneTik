package com.datahiveorg.donetik.feature.home.domain.usecase

import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository

interface GetUserInfoUseCase {
    suspend operator fun invoke(): DomainResponse<User>

}

internal class GetUserInfoUseCaseImpl(
    private val authRepository: AuthRepository,
) : GetUserInfoUseCase {
    override suspend fun invoke(): DomainResponse<User> {
        return when (val response = authRepository.getUser()) {
            is DomainResponse.Success -> {
                if (response.data != null) {
                    DomainResponse.Success(response.data)
                } else {
                    DomainResponse.Failure("No user is logged in")
                }
            }

            is DomainResponse.Failure -> {
                DomainResponse.Failure(response.message)
            }
        }
    }
}