package com.datahiveorg.donetik.core.firebase.firestore

import com.datahiveorg.donetik.core.firebase.model.FirebaseDTO.LeaderBoardUserDTO
import com.datahiveorg.donetik.core.firebase.util.Constants.IMAGE_URL_FIELD
import com.datahiveorg.donetik.core.firebase.util.Constants.LEADERBOARD_COLLECTION_ID
import com.datahiveorg.donetik.core.firebase.util.Constants.POINTS_FIELD
import com.datahiveorg.donetik.core.firebase.util.Constants.USERNAME_FIELD
import com.datahiveorg.donetik.core.firebase.util.FireStoreOperation
import com.datahiveorg.donetik.core.firebase.util.safeFireStoreCall
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

interface LeaderBoardDataSource {
    suspend fun getLeadBoard(): Result<List<LeaderBoardUserDTO>>
}


class LeaderBoardDataSourceImpl(
    private val firestore: FirebaseFirestore
) : LeaderBoardDataSource {
    private val leadBoardCollection = firestore.collection(LEADERBOARD_COLLECTION_ID)

    override suspend fun getLeadBoard(): Result<List<LeaderBoardUserDTO>> =
        safeFireStoreCall(FireStoreOperation.GET_LEADERBOARD) {
            val querySnapshot = leadBoardCollection
                .orderBy(POINTS_FIELD, Query.Direction.DESCENDING)
                .get()
                .await()

            querySnapshot.documents.mapNotNull { documentSnapshot ->
                val userId = documentSnapshot.reference.id
                LeaderBoardUserDTO(
                    uid = userId,
                    username = documentSnapshot.getString(USERNAME_FIELD) ?: "",
                    imageUrl = documentSnapshot.getString(IMAGE_URL_FIELD) ?: "",
                    points = documentSnapshot.getLong(POINTS_FIELD) ?: 0L
                )
            }
        }
}