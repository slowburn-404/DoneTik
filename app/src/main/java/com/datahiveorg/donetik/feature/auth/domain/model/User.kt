package com.datahiveorg.donetik.feature.auth.domain.model

import android.net.Uri

data class User(
    val uid: String,
    val email: String,
    val username: String,
    val imageUrl: Uri,
    val password: String
)
