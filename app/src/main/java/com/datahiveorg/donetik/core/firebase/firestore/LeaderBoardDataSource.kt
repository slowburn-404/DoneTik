package com.datahiveorg.donetik.core.firebase.firestore

import com.datahiveorg.donetik.core.firebase.model.FirebaseDTO.LeaderBoardUserDTO
import com.datahiveorg.donetik.core.firebase.util.FireStoreOperation
import com.datahiveorg.donetik.core.firebase.util.safeFireStoreCall
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

interface LeaderBoardDataSource {
    suspend fun getLeadBoard(): Result<List<LeaderBoardUserDTO>>
}


class LeaderBoardDataSourceImpl(
    private val firestore: FirebaseFirestore
) : LeaderBoardDataSource {
    override suspend fun getLeadBoard(): Result<List<LeaderBoardUserDTO>> =
        safeFireStoreCall(FireStoreOperation.GET_LEADERBOARD) {
            val snapshot = firestore.collection(USER_COLLECTION)
                .get()
                .await()
            snapshot.toObjects<LeaderBoardUserDTO>()
        }

    companion object {
        private const val USER_COLLECTION = "users"
    }
}