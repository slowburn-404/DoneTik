package com.datahiveorg.donetik.firebase.authentication

import com.datahiveorg.donetik.firebase.model.FirebaseRequest
import com.datahiveorg.donetik.util.Logger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface FirebaseAuthService {
    val isLoggedIn: Flow<Boolean>

    suspend fun createAccount(credentialsDTO: FirebaseRequest.CredentialsDTO): Result<FirebaseUser?>

    suspend fun login(credentialsDTO: FirebaseRequest.CredentialsDTO): Result<FirebaseUser?>

    suspend fun signUpWithGoogle(idToken: String): Result<FirebaseUser?>

    suspend fun fetchUserInfo(): Result<FirebaseUser?>

    suspend fun logOut(): Result<String>
}


internal class FirebaseAuthServiceImpl(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthService {

    private suspend fun <T> safeFirebaseAuthCall(
        operation: FirebaseAuthOperation,
        call: suspend () -> T
    ): Result<T> {
        return try {
            Result.success(call())
        } catch (exception: FirebaseAuthException) {
            Logger.e("FirebaseAuthService: ${operation.name}", exception.message ?: "Unknown error")
            Result.failure(exception)
        } catch (exception: Exception) {
            Logger.e("FirebaseAuthService: ${operation.name}", exception.message ?: "Unknown error")
            Result.failure(exception)
        }
    }

    override val isLoggedIn: Flow<Boolean>
        get() = callbackFlow {
            val authStateListener = FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser != null).isSuccess
            }
            firebaseAuth.addAuthStateListener(authStateListener)

            awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }

        }

    override suspend fun createAccount(credentialsDTO: FirebaseRequest.CredentialsDTO): Result<FirebaseUser?> {
        return safeFirebaseAuthCall(FirebaseAuthOperation.CREATE_ACCOUNT) {
            firebaseAuth
                .createUserWithEmailAndPassword(credentialsDTO.email, credentialsDTO.password)
                .await()
                .user
        }
    }

    override suspend fun login(credentialsDTO: FirebaseRequest.CredentialsDTO): Result<FirebaseUser?> {
        return safeFirebaseAuthCall(FirebaseAuthOperation.LOGIN) {
            firebaseAuth
                .signInWithEmailAndPassword(credentialsDTO.email, credentialsDTO.password)
                .await()
                .user
        }
    }

    override suspend fun signUpWithGoogle(idToken: String): Result<FirebaseUser?> {
        return safeFirebaseAuthCall(FirebaseAuthOperation.SIGN_IN_WITH_GOOGLE) {
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth
                .signInWithCredential(firebaseCredential)
                .await().user
        }
    }

    override suspend fun fetchUserInfo(): Result<FirebaseUser?> {
        return safeFirebaseAuthCall(FirebaseAuthOperation.FETCH_USER_INFO) {
            firebaseAuth.currentUser
        }
    }

    override suspend fun logOut(): Result<String> {
        return safeFirebaseAuthCall(FirebaseAuthOperation.LOGOUT) {
            firebaseAuth.signOut()
            "Successfully logged out"
        }
    }

}

enum class FirebaseAuthOperation{
    LOGIN,
    CREATE_ACCOUNT,
    LOGOUT,
    SIGN_IN_WITH_GOOGLE,
    FETCH_USER_INFO
}