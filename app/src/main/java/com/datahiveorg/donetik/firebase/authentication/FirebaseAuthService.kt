package com.datahiveorg.donetik.firebase.authentication

import com.datahiveorg.donetik.firebase.model.FirebaseRequest
import com.datahiveorg.donetik.firebase.model.FirebaseResponse
import com.datahiveorg.donetik.util.Logger
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface FirebaseAuthService {
    val isLoggedIn: Flow<Boolean>

    suspend fun createAccount(credentialsDTO: FirebaseRequest.CredentialsDTO): FirebaseResponse<AuthResult>

    suspend fun login(credentialsDTO: FirebaseRequest.CredentialsDTO): FirebaseResponse<AuthResult>

    suspend fun signUpWithGoogle(idToken: String): FirebaseResponse<AuthResult>

    suspend fun fetchUserInfo(): FirebaseResponse<FirebaseUser?>

    suspend fun logOut(): FirebaseResponse<String>
}


internal class FirebaseAuthServiceImpl(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthService {
    override val isLoggedIn: Flow<Boolean>
        get() = callbackFlow {
            val authStateListener = FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser != null).isSuccess
            }
            firebaseAuth.addAuthStateListener(authStateListener)

            awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }

        }

    override suspend fun createAccount(credentialsDTO: FirebaseRequest.CredentialsDTO): FirebaseResponse<AuthResult> {
        return try {
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(credentialsDTO.email, credentialsDTO.password)
                .await()

            FirebaseResponse.Success(authResult)

        } catch (exception: FirebaseAuthException) {
            Logger.e("FirebaseAuthService", exception.message ?: "Unknown error")
            FirebaseResponse.Failure(exception)
        }
    }

    override suspend fun login(credentialsDTO: FirebaseRequest.CredentialsDTO): FirebaseResponse<AuthResult> {
        return try {
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(credentialsDTO.email, credentialsDTO.password)
                .await()

            FirebaseResponse.Success(authResult)
        } catch (exception: FirebaseAuthException) {
            Logger.e("FirebaseAuthService", exception.message ?: "Unknown error")
            FirebaseResponse.Failure(exception)
        }
    }

    override suspend fun signUpWithGoogle(idToken: String): FirebaseResponse<AuthResult> {
        return try {
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth
                .signInWithCredential(firebaseCredential)
                .await()
            FirebaseResponse.Success(authResult)
        } catch (exception: FirebaseAuthInvalidUserException) {
            Logger.e("FirebaseAuthService", exception.message ?: "Unexpected error")
            FirebaseResponse.Failure(exception)
        } catch (exception: FirebaseAuthInvalidCredentialsException) {
            Logger.e("FirebaseAuthService", exception.message.toString())
            FirebaseResponse.Failure(exception)
        } catch (exception: FirebaseAuthUserCollisionException) {
            Logger.e("FirebaseAuthService", exception.message.toString())
            FirebaseResponse.Failure(exception)
        }
    }

    override suspend fun fetchUserInfo(): FirebaseResponse<FirebaseUser?> {
        return try {
            val currentUser = firebaseAuth.currentUser
            FirebaseResponse.Success(currentUser)
        } catch (exception: FirebaseAuthException) {
            Logger.e("FirebaseAuthService", exception.message ?: "Unknown error")
            FirebaseResponse.Failure(exception)
        }
    }

    override suspend fun logOut(): FirebaseResponse<String> {
        return try {
            val authResult = firebaseAuth.signOut()
            FirebaseResponse.Success(authResult.toString())
        } catch (exception: FirebaseAuthException) {
            Logger.e("FirebaseAuthenticationService", exception.message ?: "Unknown error")
            FirebaseResponse.Failure(exception)
        }
    }

}