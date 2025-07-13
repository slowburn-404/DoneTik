package com.datahiveorg.donetik.feature.leaderboard.data

import android.net.Uri
import com.datahiveorg.donetik.util.Logger
import com.datahiveorg.donetik.core.firebase.model.FirebaseDTO
import com.datahiveorg.donetik.feature.leaderboard.domain.LeaderBoardItem

fun FirebaseDTO.LeaderBoardUserDTO.toLeaderBoardDomain(): LeaderBoardItem {
    return LeaderBoardItem(
        uid = uid,
        username = username,
        points = points,
        imageUrl = imageUrl.toUri()
    )
}

fun String.toUri(): Uri {
    return if (this.isEmpty()) {
        Uri.EMPTY
    } else {
        try {
            Uri.parse(this)
        } catch (exception: Exception) {
            Logger.e("UriParsing", exception.message.toString())
            Uri.EMPTY
        }
    }
}