package com.datahiveorg.donetik.feature.home.domain

import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.domain.model.Task

interface HomeRepository {
    suspend fun getTasks(userId: String): DomainResponse<List<Task>>

    suspend fun createTask(task: Task): DomainResponse<String>

    suspend fun updateTask(task: Task): DomainResponse<String>

    suspend fun deleteTask(task: Task): DomainResponse<String>
}