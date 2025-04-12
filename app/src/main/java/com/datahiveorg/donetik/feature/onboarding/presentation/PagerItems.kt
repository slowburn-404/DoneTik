package com.datahiveorg.donetik.feature.onboarding.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.datahiveorg.donetik.R

enum class PagerItems(
    @DrawableRes val imageId: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
) {
    Todo(
        imageId = R.drawable.todo_list,
        title = R.string.screen1_title,
        description = R.string.screen1_body
    ),
    Leaderboard(
        imageId = R.drawable.leader_board,
        title = R.string.screen2_title,
        description = R.string.screen2_body
    ),
    CreateLeaderboard(
        imageId = R.drawable.share,
        title = R.string.screen3_title,
        description = R.string.screen3_body
    ),
    Finisher(
        imageId = R.drawable.finish,
        title = R.string.app_name,
        description = R.string.screen4_body
    )

}