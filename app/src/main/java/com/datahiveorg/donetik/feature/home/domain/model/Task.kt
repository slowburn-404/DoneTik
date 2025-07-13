package com.datahiveorg.donetik.feature.home.domain.model

data class Task(
    val id: String,
    val author: String,
    val title: String,
    val description: String,
    val isDone: Boolean,
    val createdAt: String,
    val dueDate: String,
    val category: String
)
