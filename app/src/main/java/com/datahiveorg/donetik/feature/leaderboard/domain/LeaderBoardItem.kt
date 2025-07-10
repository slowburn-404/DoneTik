package com.datahiveorg.donetik.feature.leaderboard.domain

import android.net.Uri

data class LeaderBoardItem(
    val uid: String,
    val username: String,
    val points: Long,
    val imageUrl: Uri
)
