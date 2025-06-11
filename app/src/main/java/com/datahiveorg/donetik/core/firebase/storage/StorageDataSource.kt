package com.datahiveorg.donetik.core.firebase.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.tasks.await

interface StorageDataSource {
    suspend fun uploadImage(imageUri: Uri, userId: String): Result<Uri>
}


class StorageDataSourceImpl(
    private val storage: FirebaseStorage,
) : StorageDataSource {

    override suspend fun uploadImage(imageUri: Uri, userId: String): Result<Uri> {
        val fileName = "$userId${System.currentTimeMillis()}.jpg"
        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpeg")
            .build()

        val imageRef = storage.reference.child(STORAGE_REF).child(fileName)
        return try {
            val uploadTask = imageRef.putFile(imageUri, metadata).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            Result.success(downloadUrl)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val STORAGE_REF = "profile_images"
    }
}