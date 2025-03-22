package com.datahiveorg.donetik.firebase.authentication

import com.datahiveorg.donetik.firebase.model.FirebaseRequest
import com.datahiveorg.donetik.firebase.model.FirebaseResponse
import com.datahiveorg.donetik.util.Logger
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface FirebaseAuthService {
    val isLoggedIn: Flow<Boolean>

    suspend fun createAccount(user: FirebaseRequest.User): FirebaseResponse<AuthResult>

    suspend fun login(user: FirebaseRequest.User): FirebaseResponse<AuthResult>

    suspend fun signUpWithGoogle(): FirebaseResponse<AuthResult>

    suspend fun fetchUserInfo(): FirebaseResponse<FirebaseUser>

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

    override suspend fun createAccount(user: FirebaseRequest.User): FirebaseResponse<AuthResult> {
        return try {
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(user.email, user.password)
                .await()

            FirebaseResponse.Success(authResult)

        } catch (exception: Exception) {
            Logger.e("FirebaseAuthService", exception.message ?: "Unknown error")
            FirebaseResponse.Failure(exception)
        } catch(exception: FirebaseAuthException) {
            FirebaseResponse.Failure(exception)
        }
    }

    override suspend fun login(user: FirebaseRequest.User): FirebaseResponse<AuthResult> {
        return try {
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(user.email, user.password)
                .await()

            FirebaseResponse.Success(authResult)
        } catch (exception: Exception) {
            Logger.e("FirebaseAuthService", exception.message ?: "Unknown error")
            FirebaseResponse.Failure(exception)
        } catch (exception: FirebaseAuthException) {
            FirebaseResponse.Failure(exception)
        }
    }

    override suspend fun signUpWithGoogle(): FirebaseResponse<AuthResult> {
        TODO("Implement google auth")
    }

    override suspend fun fetchUserInfo(): FirebaseResponse<FirebaseUser> {
        return try {
            val authResult = firebaseAuth.currentUser
            FirebaseResponse.Success(authResult ?: throw FirebaseException("User not found"))
        } catch (exception: Exception) {
            Logger.e("FirebaseAuthService", exception.message ?: "Unknown error")
            FirebaseResponse.Failure(exception)
        }catch (exception: FirebaseAuthException) {
            FirebaseResponse.Failure(exception)
        }
    }

    override suspend fun logOut(): FirebaseResponse<String> {
        return try {
            val authResult = firebaseAuth.signOut()
            FirebaseResponse.Success(authResult.toString())
        } catch (exception: Exception) {
            Logger.e("FirebaseAuthenticationService", exception.message ?: "Unknown error")
            FirebaseResponse.Failure(exception)
        } catch (exception: FirebaseAuthException) {
            FirebaseResponse.Failure(exception)
        }
    }

}