package com.datahiveorg.donetik.firebase.firestore

import com.datahiveorg.donetik.firebase.model.TaskDTO
import com.datahiveorg.donetik.util.Logger
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

interface FirebaseFireStoreService {
    suspend fun createTask(taskDTO: TaskDTO): Result<Unit>

    suspend fun deleteTask(taskDTO: TaskDTO): Result<Unit>

    suspend fun updateTask(taskDTO: TaskDTO): Result<Unit>

    suspend fun getTasks(userId: String): Result<List<TaskDTO>>
}

internal class FirebaseFireStoreServiceImpl(
    firestore: FirebaseFirestore
) : FirebaseFireStoreService {
    private val usersCollection = firestore.collection(USER_COLLECTION)

    override suspend fun createTask(taskDTO: TaskDTO): Result<Unit> {
        val taskDoc = getTaskDocument(taskDTO)

        return try {
            taskDoc.set(taskDTO).await()
            Result.success(Unit)

        } catch (exception: FirebaseFirestoreException) {
            Logger.e("FireStore", exception.message.toString())
            Result.failure(exception)
        } catch (exception: Exception) {
            Logger.e("FireStore create taskDTO", exception.message.toString())
            Result.failure(exception)
        }
    }

    override suspend fun deleteTask(taskDTO: TaskDTO): Result<Unit> {
        val taskDoc = getTaskDocument(taskDTO)

        return try {
            taskDoc.delete().await()
            Result.success(Unit)
        } catch (exception: FirebaseFirestoreException) {
            Logger.e("FireStore", exception.message.toString())
            Result.failure(exception)
        } catch (exception: Exception) {
            Logger.e("FireStore: Create taskDTO", exception.message.toString())
            Result.failure(exception)
        }
    }

    override suspend fun updateTask(taskDTO: TaskDTO): Result<Unit> {
        val taskDoc = getTaskDocument(taskDTO)

        return try {
            taskDoc.set(taskDTO).await()
            Result.success(Unit)

        } catch (exception: FirebaseFirestoreException) {
            Logger.e("FireStore", exception.message.toString())
            Result.failure(exception)
        } catch (exception: Exception) {
            Logger.e("FireStore Update TaskDTO:", exception.message.toString())
            Result.failure(exception)
        }
    }

    override suspend fun getTasks(userId: String): Result<List<TaskDTO>> {
        val tasksCollection = getTaskCollection(userId)

        return try {
            val snapshot = tasksCollection.get().await()
            val taskDTOS = snapshot.documents.mapNotNull { it.toObject(TaskDTO::class.java) }
            Result.success(taskDTOS)
        } catch (exception: FirebaseFirestoreException) {
            Logger.e("FireStore", exception.message.toString())
            Result.failure(exception)
        }
    }

    private fun getTaskDocument(taskDTO: TaskDTO): DocumentReference {
        return usersCollection
            .document(taskDTO.author.uid)
            .collection(TASKS_COLLECTION)
            .document(taskDTO.id)
    }

    private fun getTaskCollection(userId: String): CollectionReference {
        return usersCollection
            .document(userId)
            .collection(TASKS_COLLECTION)
    }

    companion object {
        private const val USER_COLLECTION = "users"
        private const val TASKS_COLLECTION = "tasks"
    }
}