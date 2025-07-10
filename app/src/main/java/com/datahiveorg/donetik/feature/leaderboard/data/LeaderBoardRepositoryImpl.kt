package com.datahiveorg.donetik.feature.leaderboard.data

import android.net.Uri
import coil3.toCoilUri
import com.datahiveorg.donetik.core.firebase.authentication.AuthDataSource
import com.datahiveorg.donetik.core.firebase.firestore.LeaderBoardDataSource
import com.datahiveorg.donetik.feature.auth.data.toAuthDomain
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.leaderboard.domain.LeaderBoardItem
import com.datahiveorg.donetik.feature.leaderboard.domain.LeaderBoardRepository
import com.datahiveorg.donetik.util.DispatcherProvider
import kotlinx.coroutines.withContext

class LeaderBoardRepositoryImpl(
    private val leaderBoardDataSource: LeaderBoardDataSource,
    private val authDataSource: AuthDataSource,
    private val dispatcherProvider: DispatcherProvider
) : LeaderBoardRepository {
    override suspend fun fetchLeaderBoard(): DomainResponse<List<LeaderBoardItem>> {
        return withContext(dispatcherProvider.io) {
            leaderBoardDataSource.getLeadBoard().fold(
                onSuccess = { data ->
                    DomainResponse.Success(
                        data.map { leaderBoardUserDTO ->
                            leaderBoardUserDTO.toLeaderBoardDomain()
                        }
                    )
                },
                onFailure = { exception ->
                    DomainResponse.Error(exception.message.toString())
                }
            )
        }
    }

    override suspend fun getCurrentUserInfo(): DomainResponse<User> {
        return withContext(dispatcherProvider.io) {
            authDataSource.fetchUserInfo().fold(
                onSuccess = { user ->
                    DomainResponse.Success(
                        user?.toAuthDomain() ?: User(
                            uid = "",
                            email = "",
                            username = "",
                            imageUrl = Uri.EMPTY,
                            password = ""
                        )
                    )
                },
                onFailure = { exception ->
                    DomainResponse.Error(exception.message.toString())
                }
            )
        }
    }
}