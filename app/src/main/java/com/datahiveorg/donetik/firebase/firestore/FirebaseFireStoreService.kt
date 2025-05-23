package com.datahiveorg.donetik.firebase.firestore

import com.datahiveorg.donetik.firebase.model.FirebaseRequest.TaskDTO
import com.datahiveorg.donetik.util.Logger
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

/**
 * Provides an abstraction layer for performing CRUD operations on user-specific tasks
 * stored in a FireStore database under the schema: users/{userId}/tasks/{taskId}.
 */
interface FirebaseFireStoreService {

    /**
     * Creates a new task in FireStore under the author's document.
     *
     * @param taskDTO A map representing the task, including a reference to the author's document.
     * @return [Result.success] if the task was successfully created, [Result.failure] otherwise.
     */
    suspend fun createTask(taskDTO: Map<String, Any>): Result<Unit>

    /**
     * Deletes a task from FireStore based on its ID and associated author's reference.
     *
     * @param taskDTO A map containing the task's ID and author reference.
     * @return [Result.success] if the task was successfully deleted, [Result.failure] otherwise.
     */
    suspend fun deleteTask(taskDTO: Map<String, Any>): Result<Unit>

    /**
     * Updates an existing task in FireStore using merge semantics to retain existing data.
     *
     * @param taskDTO A map representing the task update, including a reference to the author's document.
     * @return [Result.success] if the update was successful, [Result.failure] otherwise.
     */
    suspend fun updateTask(taskDTO: Map<String, Any>): Result<Unit>

    /**
     * Retrieves all tasks belonging to a specific user, ordered by their creation time.
     *
     * @param userId The ID of the user whose tasks are to be fetched.
     * @return A [Result] containing the list of [TaskDTO]s or an error.
     */
    suspend fun getTasks(userId: String): Result<List<TaskDTO>>

    /**
     * Fetches a single task by its ID under a specified user's document.
     *
     * @param userId The ID of the user who owns the task.
     * @param taskId The ID of the task to retrieve.
     * @return A [Result] containing the [TaskDTO] or an error.
     */
    suspend fun getSingleTask(userId: String, taskId: String): Result<TaskDTO>

    suspend fun markTaskAsDone(userId: String, taskId: String, isDone: Boolean): Result<Unit>
}


internal class FirebaseFireStoreServiceImpl(
    firestore: FirebaseFirestore
) : FirebaseFireStoreService {
    private val usersCollection = firestore.collection(USER_COLLECTION)

    private suspend fun <T> safeFireStoreCall(
        operation: FireStoreOperation,
        call: suspend () -> T
    ): Result<T> = try {
        Result.success(call())
    } catch (e: FirebaseFirestoreException) {
        Logger.e("$TAG ${operation.name}", e.message.orEmpty())
        Result.failure(e)
    } catch (e: Exception) {
        Logger.e("$TAG ${operation.name}", e.message.orEmpty())
        Result.failure(e)
    }

    override suspend fun createTask(taskDTO: Map<String, Any>): Result<Unit> =
        safeFireStoreCall(FireStoreOperation.CREATE_TASK) {
            getTaskDocumentReference(taskDTO)
                .set(taskDTO)
                .await()
        }

    override suspend fun updateTask(taskDTO: Map<String, Any>): Result<Unit> =
        safeFireStoreCall(FireStoreOperation.UPDATE_TASK) {
            getTaskDocumentReference(taskDTO)
                .set(taskDTO, SetOptions.merge())
                .await()
        }

    override suspend fun deleteTask(taskDTO: Map<String, Any>): Result<Unit> =
        safeFireStoreCall(FireStoreOperation.DELETE_TASK) {
            getTaskDocumentReference(taskDTO)
                .delete()
                .await()
        }

    override suspend fun getTasks(userId: String): Result<List<TaskDTO>> =
        safeFireStoreCall(FireStoreOperation.GET_TASKS) {
            val snapshot = getTaskCollectionReference(userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toTaskDTO() }
        }

    override suspend fun getSingleTask(userId: String, taskId: String): Result<TaskDTO> =
        safeFireStoreCall(FireStoreOperation.GET_SINGLE_TASK) {
            val doc = getTaskCollectionReference(userId)
                .document(taskId)
                .get()
                .await()

            if (!doc.exists()) throw NoSuchElementException("Task not found: \$taskId")

            doc.toTaskDTO()
        }

    override suspend fun markTaskAsDone(
        userId: String,
        taskId: String,
        isDone: Boolean
    ): Result<Unit> =
        safeFireStoreCall(FireStoreOperation.MARK_AS_DONE) {
            getTaskCollectionReference(userId)
                .document(taskId)
                .update("isDone", isDone)
                .await()
        }


    private fun getTaskDocumentReference(taskDTO: Map<String, Any>): DocumentReference {
        val authorMap = taskDTO["author"] as? Map<*, *>
            ?: throw IllegalArgumentException("Missing or invalid user object")
        val userId = authorMap["uid"] as? String
            ?: throw IllegalArgumentException("Missing or invalid user id")
        val taskId = taskDTO["id"] as? String
            ?: throw IllegalArgumentException("Missing or invalid task id")
        return usersCollection
            .document(userId)
            .collection(TASKS_COLLECTION)
            .document(taskId)
    }

    private fun getTaskCollectionReference(userId: String): CollectionReference =
        usersCollection.document(userId).collection(TASKS_COLLECTION)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val TASKS_COLLECTION = "tasks"
        private const val TAG = "FireStoreService:"
    }
}

enum class FireStoreOperation {
    CREATE_TASK,
    UPDATE_TASK,
    DELETE_TASK,
    GET_TASKS,
    GET_SINGLE_TASK,
    MARK_AS_DONE
}