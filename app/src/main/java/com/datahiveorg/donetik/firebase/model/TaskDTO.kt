package com.datahiveorg.donetik.firebase.model

import com.google.firebase.auth.FirebaseUser
import java.util.Date

data class TaskDTO(
    val id: String,
    val author: FirebaseUser,
    val title: String,
    val description: String,
    @field: JvmField val isDone: Boolean,
    val created: Date,
    val lastModified: Date
)
