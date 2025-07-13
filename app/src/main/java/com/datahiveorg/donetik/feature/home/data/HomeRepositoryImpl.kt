package com.datahiveorg.donetik.feature.home.data

import com.datahiveorg.donetik.core.firebase.authentication.AuthDataSource
import com.datahiveorg.donetik.core.firebase.firestore.TasksDataSource
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.domain.model.Task

class HomeRepositoryImpl(
    private val tasksDataSource: TasksDataSource,
    private val authDataSource: AuthDataSource
) : HomeRepository {
    override suspend fun getTasks(userId: String): DomainResponse<List<Task>> {
        val response = tasksDataSource.getTasks(userId)

        return response.fold(
            onSuccess = { taskDTOs ->
                val tasks = taskDTOs.map { it.toTask() }
                DomainResponse.Success(tasks)
            },
            onFailure = { exception ->
                DomainResponse.Error(exception.toDomain())
            }
        )
    }

    override suspend fun createTask(task: Task): DomainResponse<String> {
        val response = tasksDataSource.createTask(task.toTaskDTO())

        return response.fold(
            onSuccess = {
                DomainResponse.Success("Task saved. What’s next?")
            },
            onFailure = {
                DomainResponse.Error(it.toDomain())
            }
        )
    }

    override suspend fun updateTask(task: Task): DomainResponse<String> {
        val response = tasksDataSource.updateTask(task.toTaskDTO())

        return response.fold(
            onSuccess = {
                DomainResponse.Success("Task updated")
            },
            onFailure = {
                DomainResponse.Error(it.toDomain())
            }
        )
    }

    override suspend fun deleteTask(task: Task): DomainResponse<String> {
        val response = tasksDataSource.deleteTask(task.toTaskDTO())

        return response.fold(
            onSuccess = {
                DomainResponse.Success("Task deleted")
            },
            onFailure = {
                DomainResponse.Error(it.toDomain())
            }
        )
    }

    override suspend fun getSingleTask(taskId: String, userId: String): DomainResponse<Task> {
        val response = tasksDataSource.getSingleTask(userId, taskId)
        return response.fold(
            onSuccess = { taskDTO ->
                DomainResponse.Success(taskDTO.toTask())
            },
            onFailure = { exception ->
                DomainResponse.Error(exception.toDomain())
            }
        )
    }

    override suspend fun markTaskAsDone(task: Task, user: User): DomainResponse<String> {
        val response = tasksDataSource.markTaskAsDone(
            userDTO = user.toUserDTO(),
            taskDTO = task.toTaskDTO()
        )
        return response.fold(
            onSuccess = {
                DomainResponse.Success(if (task.isDone) "Marked as done" else "Marked as undone")
            },
            onFailure = { exception ->
                DomainResponse.Error(exception.toDomain())
            }
        )
    }

    override suspend fun getCurrentUserInfo(): DomainResponse<User> {
        val response = authDataSource.fetchUserInfo()

        return response.fold(
            onSuccess = { userDTO ->
                val firebaseUser =
                    userDTO ?: return@fold DomainResponse.Error("User not found")

                DomainResponse.Success(firebaseUser.toUser())
            },
            onFailure = { exception ->
                DomainResponse.Error(exception.toDomain())
            }
        )

    }
}