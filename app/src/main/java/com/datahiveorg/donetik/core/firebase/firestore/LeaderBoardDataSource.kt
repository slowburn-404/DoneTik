package com.datahiveorg.donetik.core.firebase.firestore

import com.datahiveorg.donetik.core.firebase.model.FirebaseDTO.LeaderBoardUserDTO
import com.datahiveorg.donetik.core.firebase.util.FireStoreOperation
import com.datahiveorg.donetik.core.firebase.util.safeFireStoreCall
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

interface LeaderBoardDataSource {
    suspend fun getLeadBoard(): Result<List<LeaderBoardUserDTO>>
}


class LeaderBoardDataSourceImpl(
    private val firestore: FirebaseFirestore
) : LeaderBoardDataSource {
    override suspend fun getLeadBoard(): Result<List<LeaderBoardUserDTO>> =
        safeFireStoreCall(FireStoreOperation.GET_LEADERBOARD) {
            val querySnapshot = firestore
                .collectionGroup(USER_PUBLIC_COLLECTION_ID)
                .whereEqualTo(FieldPath.documentId(), USER_PROFILE_DOCUMENT_ID)
                .orderBy(POINTS_FIELD, Query.Direction.DESCENDING)
                .get()
                .await()

            querySnapshot.documents.mapNotNull { documentSnapshot ->
                val userId = documentSnapshot.reference.parent.parent?.id
                val profileDoc = documentSnapshot.data

                profileDoc?.let {
                    LeaderBoardUserDTO(
                        uid = userId ?: return@mapNotNull null,
                        username = it[USERNAME_FIELD] as? String ?: return@mapNotNull null,
                        points = it[POINTS_FIELD] as? Long ?: return@mapNotNull null,
                        imageUrl = it[IMAGE_URL_FIELD] as? String ?: return@mapNotNull null
                    )
                }
            }
        }

    companion object {
        private const val USER_PUBLIC_COLLECTION_ID = "public"
        private const val USER_PROFILE_DOCUMENT_ID = "profile"
        private const val POINTS_FIELD = "points"
        private const val USERNAME_FIELD = "username"
        private const val IMAGE_URL_FIELD = "imageUrl"
    }
}