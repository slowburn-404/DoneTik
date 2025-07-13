package com.datahiveorg.donetik.feature.home.domain

import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.domain.model.Task

/**
 * Repository interface for managing tasks.
 * This interface defines the contract for interacting with the data layer for task-related operations.
 */
interface HomeRepository {
    suspend fun getTasks(userId: String): DomainResponse<List<Task>>

    suspend fun createTask(task: Task): DomainResponse<String>

    suspend fun updateTask(task: Task): DomainResponse<String>

    suspend fun deleteTask(task: Task): DomainResponse<String>

    suspend fun getSingleTask(taskId: String, userId: String): DomainResponse<Task>

    suspend fun markTaskAsDone(task: Task, user: User): DomainResponse<String>

    suspend fun getCurrentUserInfo(): DomainResponse<User>
}