package com.datahiveorg.donetik.feature.home.data


import coil3.toCoilUri
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.core.firebase.model.FirebaseDTO.TaskDTO
import com.datahiveorg.donetik.core.firebase.model.FirebaseDTO.UserDTO
import com.datahiveorg.donetik.feature.leaderboard.data.toUri
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Converts a [TaskDTO] to a [Task] domain model.
 *
 * This extension function maps the properties of a Data Transfer Object (DTO)
 * representing a task to the corresponding properties of a `Task` object
 * used in the application's domain layer.
 *
 * @return A [Task] object populated with data from the [TaskDTO].
 */
fun TaskDTO.toHomeDomain(): Task {
    return Task(
        id = id,
        author = author.toHomeDomain(),
        title = title,
        description = description,
        isDone = isDone,
        createdAt = createdAt.toDate().toHomeDomain(),
        dueDate = dueDate.toDate().toHomeDomain(),
        category = category
    )
}

fun Throwable.toHomeDomain(): String {
    return this.message.toString()
}

/**
 * Converts a [Task] object to a [Map] that can be stored in Firebase.
 *
 * @return A [Map] representing the [Task] object.
 */
fun Task.toFireBase(): Map<String, Any> = mapOf(
    "id" to id,
    "author" to author.toUserDTO(),
    "title" to title,
    "description" to description,
    "isDone" to isDone,
    "createdAt" to createdAt.toFireStoreTimeStamp(),
    "dueDate" to dueDate.toFireStoreTimeStamp(),
    "category" to category
)

/**
 * Converts a [User] object to a [Map] representation suitable for DTO purposes.
 * This map includes the user's UID, username, email, and image URL.
 *
 * @return A [Map] where keys are property names ("uid", "username", "email", "imageUrl")
 *         and values are the corresponding properties of the [User] object.
 */
fun User.toUserDTO(): Map<String, Any> {
    return mapOf(
        "uid" to uid,
        "username" to username,
        "email" to email,
        "imageUrl" to imageUrl.toString()
    )
}

/**
 * Converts a [UserDTO] to a [User] domain model.
 *
 * This function maps the fields from the DTO to the corresponding fields in the domain model.
 * The `password` field in the domain model is initialized as an empty string because it's
 * not typically transferred in this DTO and is handled separately for security reasons.
 *
 * @return A [User] object representing the domain model.
 */
fun UserDTO.toHomeDomain(): User {
    return User(
        uid = uid,
        username = username,
        email = email,
        imageUrl = imageUrl.toUri(),
        password = ""
    )
}


/**
 * Converts a Date object to a formatted string representation suitable for display in the home domain.
 * The format used is "dd MMM yyyy, HH:mm" based on the default Locale and TimeZone.
 *
 * @return A string representing the formatted date.
 *
 * */
 //TODO: Move to a more common place
fun Date.toHomeDomain(): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(this)
}

/**
 * Converts a string representation of a date to a [Date] object.
 *
 * The expected string format is "dd MMM yyyy, HH:mm" (e.g., "25 Dec 2023, 10:30").
 * The parsing is done assuming the input string represents a date and time in the host's default timezone.
 *
 * @receiver The string to be converted to a [Date].
 * @return The parsed [Date] object.
 * @throws java.text.ParseException if the string cannot be parsed into the expected date format.
 * @throws NullPointerException if the parsing results in a null Date (this should be handled, as indicated by the TODO).
 */
fun String.toDate(): Date {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.parse(this)!! // TODO: remember to handle null dates
}

/**
 * Converts a string representation of a date and time into a Firebase Timestamp.
 *
 * This function first parses the string into a [Date] object using the [toDate] extension function,
 * and then creates a [Timestamp] from that [Date].
 *
 * @receiver The string to convert, expected to be in the format "dd MMM yyyy, HH:mm".
 * @return A [Timestamp] object representing the same date and time.
 * @see toDate
 */
fun String.toFireStoreTimeStamp(): Timestamp {
    return Timestamp(this.toDate())
}