package com.datahiveorg.donetik.feature.home.domain.model

import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.google.firebase.auth.FirebaseUser
import java.util.Date

data class Task(
    val id: String,
    val author: FirebaseUser,
    val title: String,
    val description: String,
    val isDone: Boolean,
    val createdAt: Date,
    val lastModified: Date
)
