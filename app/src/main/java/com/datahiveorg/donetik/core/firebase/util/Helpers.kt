package com.datahiveorg.donetik.core.firebase.util

import com.google.firebase.firestore.FirebaseFirestoreException
import com.datahiveorg.donetik.util.Logger

suspend fun <T> safeFireStoreCall(
    operation: FireStoreOperation,
    call: suspend () -> T
): Result<T> = try {
    Result.success(call())
} catch (e: FirebaseFirestoreException) {
    Logger.e("${operation.name}:", e.message.orEmpty())
    Result.failure(e)
} catch (e: Exception) {
    Logger.e("${operation.name}:", e.message.orEmpty())
    Result.failure(e)
}
