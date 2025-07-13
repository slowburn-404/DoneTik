package com.datahiveorg.donetik.feature.leaderboard.domain

import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User

interface LeaderBoardRepository {
    suspend fun fetchLeaderBoard(): DomainResponse<List<LeaderBoardItem>>

    suspend fun getCurrentUserInfo(): DomainResponse<User>
}