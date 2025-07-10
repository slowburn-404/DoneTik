package com.datahiveorg.donetik.feature.home.data

import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.core.firebase.firestore.TasksDataSource

class HomeRepositoryImpl(
    private val tasksDataSource: TasksDataSource
) : HomeRepository {
    override suspend fun getTasks(userId: String): DomainResponse<List<Task>> {
        val response = tasksDataSource.getTasks(userId)

        return response.fold(
            onSuccess = { taskDTOs ->
                val tasks = taskDTOs.map { it.toHomeDomain() }
                DomainResponse.Success(tasks)
            },
            onFailure = { exception ->
                DomainResponse.Error(exception.toHomeDomain())
            }
        )
    }

    override suspend fun createTask(task: Task): DomainResponse<String> {
        val response = tasksDataSource.createTask(task.toFireBase())

        return response.fold(
            onSuccess = {
                DomainResponse.Success("Task saved. What’s next?")
            },
            onFailure = {
                DomainResponse.Error(it.toHomeDomain())
            }
        )
    }

    override suspend fun updateTask(task: Task): DomainResponse<String> {
        val response = tasksDataSource.updateTask(task.toFireBase())

        return response.fold(
            onSuccess = {
                DomainResponse.Success("Task updated")
            },
            onFailure = {
                DomainResponse.Error(it.toHomeDomain())
            }
        )
    }

    override suspend fun deleteTask(task: Task): DomainResponse<String> {
        val response = tasksDataSource.deleteTask(task.toFireBase())

        return response.fold(
            onSuccess = {
                DomainResponse.Success("Task deleted")
            },
            onFailure = {
                DomainResponse.Error(it.toHomeDomain())
            }
        )
    }

    override suspend fun getSingleTask(taskId: String, userId: String): DomainResponse<Task> {
        val response = tasksDataSource.getSingleTask(userId, taskId)
        return response.fold(
            onSuccess = { taskDTO ->
                DomainResponse.Success(taskDTO.toHomeDomain())
            },
            onFailure = { exception ->
                DomainResponse.Error(exception.toHomeDomain())
            }
        )
    }

    override suspend fun markTaskAsDone(task: Task): DomainResponse<String> {
        val response = tasksDataSource.markTaskAsDone(
            userDTO = task.author.toUserDTO(),
            taskId = task.id,
            isDone = task.isDone
        )
        return response.fold(
            onSuccess = { DomainResponse.Success("Done status changed") },
            onFailure = { exception ->
                DomainResponse.Error(exception.toHomeDomain())
            }
        )
    }
}