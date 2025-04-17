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
                DomainResponse.Success("Task created successfully")
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
                DomainResponse.Success("Marked as done")
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
}