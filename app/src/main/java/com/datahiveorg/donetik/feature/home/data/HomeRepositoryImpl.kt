package com.datahiveorg.donetik.feature.home.data

import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.firebase.firestore.FirebaseFireStoreService

class HomeRepositoryImpl(
    private val fireStoreService: FirebaseFireStoreService
) : HomeRepository {
    override suspend fun getTasks(userId: String): DomainResponse<List<Task>> {
        val response = fireStoreService.getTasks(userId)

        return response.fold(
            onSuccess = { taskDTOs ->
                val tasks = taskDTOs.map { it.toDomain() }
                DomainResponse.Success(tasks)
            },
            onFailure = { exception ->
                DomainResponse.Failure(exception.toDomain())
            }
        )
    }

    override suspend fun createTask(task: Task): DomainResponse<String> {
        val response = fireStoreService.createTask(task.toFireBase())

        return response.fold(
            onSuccess = {
                DomainResponse.Success("Task saved. What’s next?")
            },
            onFailure = {
                DomainResponse.Failure(it.toDomain())
            }
        )
    }

    override suspend fun updateTask(task: Task): DomainResponse<String> {
        val response = fireStoreService.updateTask(task.toFireBase())

        return response.fold(
            onSuccess = {
                DomainResponse.Success("Task updated")
            },
            onFailure = {
                DomainResponse.Failure(it.toDomain())
            }
        )
    }

    override suspend fun deleteTask(task: Task): DomainResponse<String> {
        val response = fireStoreService.deleteTask(task.toFireBase())

        return response.fold(
            onSuccess = {
                DomainResponse.Success("Task deleted")
            },
            onFailure = {
                DomainResponse.Failure(it.toDomain())
            }
        )
    }

    override suspend fun getSingleTask(taskId: String, userId: String): DomainResponse<Task> {
        val response = fireStoreService.getSingleTask(userId, taskId)
        return response.fold(
            onSuccess = { taskDTO ->
                DomainResponse.Success(taskDTO.toDomain())
            },
            onFailure = { exception ->
                DomainResponse.Failure(exception.toDomain())
            }
        )
    }

    override suspend fun markTaskAsDone(task: Task): DomainResponse<String> {
        val response = fireStoreService.markTaskAsDone(
            userId = task.author.uid,
            taskId = task.id,
            isDone = task.isDone
        )
        return response.fold(
            onSuccess = { DomainResponse.Success("Done status changed") },
            onFailure = { exception ->
                DomainResponse.Failure(exception.toDomain())
            }
        )
    }
}