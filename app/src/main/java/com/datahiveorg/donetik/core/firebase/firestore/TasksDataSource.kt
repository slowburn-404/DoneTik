package com.datahiveorg.donetik.core.firebase.firestore

import com.datahiveorg.donetik.core.firebase.model.FirebaseDTO.TaskDTO
import com.datahiveorg.donetik.core.firebase.util.FireStoreOperation
import com.datahiveorg.donetik.core.firebase.util.safeFireStoreCall
import com.datahiveorg.donetik.util.Logger
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

/**
 * Provides an abstraction layer for performing CRUD operations on user-specific tasks
 * stored in a FireStore database under the schema: users/{userId}/tasks/{taskId}.
 */
interface TasksDataSource {

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

    /**
     * Marks a task as done or not done.
     *
     * @param authorDTO The ID of the user who owns the task.
     * @param taskId The ID of the task to update.
     * @param isDone A boolean indicating whether the task is done or not.
     * @return A [Result] indicating success or failure.
     */
    suspend fun markTaskAsDone(
        userDTO: Map<String, Any>,
        taskId: String,
        isDone: Boolean
    ): Result<Unit>
}


internal class TasksDataSourceImpl(
    private val firestore: FirebaseFirestore
) : TasksDataSource {
    private val usersCollection = firestore.collection(USER_COLLECTION)

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
        userDTO: Map<String, Any>,
        taskId: String,
        isDone: Boolean
    ): Result<Unit> =
        safeFireStoreCall(FireStoreOperation.MARK_AS_DONE) {
            Logger.i(TAG, userDTO.toString())
            val userId = userDTO["uid"] as? String
            val username = userDTO["username"] as? String
            val imageUrl = userDTO["imageUrl"] as? String

            if (userId == null) throw FirebaseException("Missing user information")

            val taskDocRef = getTaskCollectionReference(userId)
                .document(taskId)
            val userPublicProfileDocRef = usersCollection.document(userId)
                .collection(PUBLIC_USER_COLLECTION)
                .document(PROFILE_DOCUMENT)

            firestore.runTransaction { transaction ->
                val taskSnapshot = transaction.get(taskDocRef)

                if (!taskSnapshot.exists()) throw NoSuchElementException("Task not found")

                val profileSnapshot = transaction.get(userPublicProfileDocRef)

                val currentTaskIsDone = taskSnapshot.getBoolean("isDone") ?: false

                if (currentTaskIsDone == isDone) return@runTransaction

                transaction.update(taskDocRef, "isDone", isDone)

                if (!profileSnapshot.exists()) {
                    val newProfileData = mutableMapOf<String, Any>()
                    if (username != null ) newProfileData["username"] = username
                    if (imageUrl != null ) newProfileData["imageUrl"] = imageUrl
                    newProfileData["points"] =
                        if (isDone) POINTS_FOR_COMPLETING_TASK.toLong() else 0L
                    transaction.set(
                        userPublicProfileDocRef,
                        newProfileData
                    )
                    return@runTransaction
                }
                val currentPoints = profileSnapshot.getLong("points") ?: 0L
                val pointsChange =
                    calculatePointsChange(currentPoints = currentPoints, isTaskDone = isDone)

                transaction.update(
                    userPublicProfileDocRef,
                    "points",
                    FieldValue.increment(pointsChange)
                )
            }.await()

        }

    private fun calculatePointsChange(currentPoints: Long, isTaskDone: Boolean): Long {
        return (if (isTaskDone) {
            POINTS_FOR_COMPLETING_TASK.toLong()
        } else {
            when {
                currentPoints <= 0L -> 0L
                else -> -POINTS_FOR_COMPLETING_TASK.toLong()
            }
        })
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
        private const val PUBLIC_USER_COLLECTION = "public"
        private const val PROFILE_DOCUMENT = "profile"
        private const val POINTS_FOR_COMPLETING_TASK = 1
        private const val TAG = "TasksDataSource:"
    }
}