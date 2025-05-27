package com.datahiveorg.donetik.feature.home.domain.model

import com.datahiveorg.donetik.feature.auth.domain.model.User

data class Task(
    val id: String,
    val author: User,
    val title: String,
    val description: String,
    val isDone: Boolean,
    val createdAt: String,
    val dueDate: String,
    val category: String
)
