package com.datahiveorg.donetik.core.firebase.firestore

import com.datahiveorg.donetik.core.firebase.model.FirebaseDTO.TaskDTO
import com.datahiveorg.donetik.core.firebase.util.Constants.LEADERBOARD_COLLECTION_ID
import com.datahiveorg.donetik.core.firebase.util.Constants.POINTS_FOR_COMPLETING_TASK
import com.datahiveorg.donetik.core.firebase.util.Constants.TASKS_COLLECTION
import com.datahiveorg.donetik.core.firebase.util.Constants.USER_COLLECTION
import com.datahiveorg.donetik.core.firebase.util.FireStoreOperation
import com.datahiveorg.donetik.core.firebase.util.safeFireStoreCall
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
            val taskId =
                taskDTO["id"] as? String ?: throw FirebaseException("Missing taskId")
            val userIdPerformingAction = userDTO["uid"] ?: throw FirebaseException("Missing userId")
            val authorId =
                taskDTO["author"] as? String ?: throw FirebaseException("Missing authorId")
            val username = userDTO["username"] as? String ?: ""
            val imageUrl = userDTO["imageUrl"] as? String ?: ""
            val newIsDoneState = taskDTO["isDone"] as? Boolean
                ?: throw IllegalArgumentException("Missing task isDone information")

            val taskDocRef = getTaskCollectionReference(authorId)
                .document(taskId)
            val leaderBoardItemDocRef =
                firestore.collection(LEADERBOARD_COLLECTION_ID)
                    .document(userIdPerformingAction as String)

            firestore.runTransaction { transaction ->
                val taskSnapshot = transaction.get(taskDocRef)
                val leaderBoardItemSnapshot = transaction.get(leaderBoardItemDocRef)

                if (!taskSnapshot.exists()) throw NoSuchElementException("Task not found")

                val isCurrentTaskDone = taskSnapshot.getBoolean("isDone") ?: false

                val taskStateChanging = isCurrentTaskDone != newIsDoneState
                var pointsChange = 0L

                if (taskStateChanging) {
                    pointsChange = if (newIsDoneState) {
                        POINTS_FOR_COMPLETING_TASK.toLong()
                    } else {
                        val currentPoints = leaderBoardItemSnapshot.getLong("points") ?: 0L
                        if (currentPoints > 0) {
                            -POINTS_FOR_COMPLETING_TASK.toLong()
                        } else {
                            0L
                        }
                    }
                }


                if (taskStateChanging) {
                    transaction.update(taskDocRef, "isDone", newIsDoneState)
                }

                if (!leaderBoardItemSnapshot.exists()) {
                    val newLeaderBoardEntry = mutableMapOf<String, Any>(
                        "username" to username,
                        "imageUrl" to imageUrl,
                        "points" to if (pointsChange > 0L) pointsChange else 0L
                    )
                    transaction.set(
                        leaderBoardItemDocRef,
                        newLeaderBoardEntry
                    )
                } else {
                    val updatesForLeaderBoard = mutableMapOf<String, Any>()
                    if (pointsChange != 0L) {
                        updatesForLeaderBoard["points"] = FieldValue.increment(pointsChange)
                    }
                    if (username.isNotEmpty()) updatesForLeaderBoard["username"] = username
                    if (imageUrl.isNotEmpty()) updatesForLeaderBoard["imageUrl"] = imageUrl

                    if (updatesForLeaderBoard.isNotEmpty()) {
                        transaction.set(
                            leaderBoardItemDocRef,
                            updatesForLeaderBoard,
                            SetOptions.merge()
                        )
                    }
                }

            }.await()
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