package com.datahiveorg.donetik.core.firebase.firestore

import com.datahiveorg.donetik.core.firebase.model.FirebaseDTO.TaskDTO
import com.datahiveorg.donetik.core.firebase.util.Constants.LEADERBOARD_COLLECTION_ID
import com.datahiveorg.donetik.core.firebase.util.Constants.POINTS_FOR_COMPLETING_TASK
import com.datahiveorg.donetik.core.firebase.util.Constants.TAG
import com.datahiveorg.donetik.core.firebase.util.Constants.TASKS_COLLECTION
import com.datahiveorg.donetik.core.firebase.util.Constants.USER_COLLECTION
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
     * Marks a task as done or not done and updates the user's points in the leaderboard.
     *
     * This function performs a transaction to ensure atomicity:
     * 1. Updates the `isDone` status of the specified task.
     * 2. If the user does not exist in the leaderboard, creates a new entry with initial points.
     * 3. If the user exists, updates their points based on whether the task is being marked as done or undone.
     *    - If marked as done, `POINTS_FOR_COMPLETING_TASK` are added.
     *    - If marked as not done, `POINTS_FOR_COMPLETING_TASK` are subtracted (points won't go below 0).
     *
     * @param userDTO A map containing user information, expecting "uid" (String), "username" (String, optional),
     * and "imageUrl" (String, optional).
     * @param taskDTO A map containing task information, expecting "id" (String) for the task ID.
     *                The "isDone" field in this DTO determines the new state of the task.
     * @return A [Result] indicating success ([Result.success] with [Unit]) or failure ([Result.failure] with an Exception).
     * @throws FirebaseException if essential user information ("uid") is missing from `userDTO`.
     * @throws NoSuchElementException if the specified task is not found.
     * @throws IllegalArgumentException if task "id" is missing from `taskDTO`.
     */
    suspend fun markTaskAsDone(
        userDTO: Map<String, Any>,
        taskDTO: Map<String, Any>
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
        taskDTO: Map<String, Any>
    ): Result<Unit> =
        safeFireStoreCall(FireStoreOperation.MARK_AS_DONE) {
            Logger.i(TAG, userDTO.toString())
            val taskId =
                taskDTO["id"] as? String ?: throw FirebaseException("Missing task information")
            val userId = userDTO["uid"] ?: throw FirebaseException("Missing user information")
            val userIdFromTask = taskDTO["author"] as? String ?: ""
            val username = userDTO["username"] as? String ?: ""
            val imageUrl = userDTO["imageUrl"] as? String ?: ""
            val isDone = taskDTO["isDone"] as? Boolean ?: false

            val taskDocRef = getTaskCollectionReference(userIdFromTask)
                .document(taskId)
            val leaderBoardItemDocRef =
                firestore.collection(LEADERBOARD_COLLECTION_ID)
                    .document(userId.toString())

            firestore.runTransaction { transaction ->
                val taskSnapshot = transaction.get(taskDocRef)

                if (!taskSnapshot.exists()) throw NoSuchElementException("Task not found")

                val leaderBoardItemSnapshot = transaction.get(leaderBoardItemDocRef)

                val isCurrentTaskDone = taskSnapshot.getBoolean("isDone") ?: false

                if (isCurrentTaskDone == isDone) return@runTransaction

                transaction.update(taskDocRef, "isDone", isDone)

                val leaderBoardDTO = mutableMapOf<String, Any>()
                leaderBoardDTO["username"] = username
                leaderBoardDTO["imageUrl"] = imageUrl
                leaderBoardDTO["points"] =
                    if (isDone) POINTS_FOR_COMPLETING_TASK.toLong() else 0L

                transaction.set(
                    leaderBoardItemDocRef,
                    leaderBoardDTO,
                    SetOptions.merge()
                )
                val currentPoints = leaderBoardItemSnapshot.getLong("points") ?: 0L
                val pointsChange =
                    calculatePointsChange(currentPoints = currentPoints, isTaskDone = isDone)

                transaction.update(
                    leaderBoardItemDocRef,
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
        val userId = taskDTO["author"] as? String
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

}