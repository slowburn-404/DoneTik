package com.datahiveorg.donetik.firebase.firestore

import com.datahiveorg.donetik.firebase.model.FirebaseRequest.TaskDTO
import com.datahiveorg.donetik.util.Logger
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

interface FirebaseFireStoreService {
    suspend fun createTask(taskDTO: Map<String, Any>): Result<Unit>

    suspend fun deleteTask(taskDTO: Map<String, Any>): Result<Unit>

    suspend fun updateTask(taskDTO: Map<String, Any>): Result<Unit>

    suspend fun getTasks(userId: String): Result<List<TaskDTO>>
}

internal class FirebaseFireStoreServiceImpl(
    firestore: FirebaseFirestore
) : FirebaseFireStoreService {
    private val usersCollection = firestore.collection(USER_COLLECTION)

    override suspend fun createTask(taskDTO: Map<String, Any>): Result<Unit> {
        val taskDoc = getTaskDocumentReference(taskDTO)

        return try {
            taskDoc.set(taskDTO).await()
            Result.success(Unit)

        } catch (exception: FirebaseFirestoreException) {
            Logger.e("FireStore create task", exception.message.toString())
            Result.failure(exception)
        } catch (exception: Exception) {
            Logger.e("FireStore create task", exception.message.toString())
            Result.failure(exception)
        }
    }

    override suspend fun deleteTask(taskDTO: Map<String, Any>): Result<Unit> {
        val taskDoc = getTaskDocumentReference(taskDTO)

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

    override suspend fun updateTask(taskDTO: Map<String, Any>): Result<Unit> {
        val taskDoc = getTaskDocumentReference(taskDTO)

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
        val tasksCollection = getTaskCollectionReference(userId)

        return try {
            val snapshot = tasksCollection.get().await()
            val taskDTOS = snapshot.documents.mapNotNull { it.toObject(TaskDTO::class.java) }
            Result.success(taskDTOS)
        } catch (exception: FirebaseFirestoreException) {
            Logger.e("FireStore", exception.message.toString())
            Result.failure(exception)
        }
    }

    private fun getTaskDocumentReference(taskDTO: Map<String, Any>): DocumentReference {
        val authorMap = taskDTO["author"] as Map<*, *>
        return usersCollection
            .document(authorMap["uid"] as String)
            .collection(TASKS_COLLECTION)
            .document(taskDTO["id"] as String)
    }

    private fun getTaskCollectionReference(userId: String): CollectionReference {
        return usersCollection
            .document(userId)
            .collection(TASKS_COLLECTION)
    }

    companion object {
        private const val USER_COLLECTION = "users"
        private const val TASKS_COLLECTION = "tasks"
    }
}