package com.datahiveorg.donetik.core.firebase.authentication

import com.datahiveorg.donetik.core.firebase.model.FirebaseRequest
import com.datahiveorg.donetik.util.Logger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Interface for managing user authentication with Firebase.
 *
 * This interface defines the contract for all authentication-related operations,
 * such as creating accounts, logging in, signing up with Google, fetching user information,
 * and logging out. It also provides a Flow to observe the user's login state.
 */
interface AuthDataSource {
    /**
     * A [Flow] that emits `true` if the user is currently logged in, and `false` otherwise.
     * This Flow will emit a new value whenever the authentication state changes.
     */
    val isLoggedIn: Flow<Boolean>

    /**
     * Creates a new user account with the given email and password.
     *
     * @param credentialsDTO The data transfer object containing the email and password for the new account.
     * @return A [Result] object that encapsulates either the successfully created [FirebaseUser] or an [Exception] if the operation failed.
     */
    suspend fun createAccount(credentialsDTO: FirebaseRequest.CredentialsDTO): Result<FirebaseUser?>

    /**
     * Attempts to log in a user with the provided email and password credentials.
     *
     * @param credentialsDTO A [FirebaseRequest.CredentialsDTO] object containing the user's email and password.
     * @return A [Result] object.
     *         On success, it contains the [FirebaseUser] object representing the logged-in user.
     *         On failure, it contains an [Exception] detailing the error.
     */
    suspend fun login(credentialsDTO: FirebaseRequest.CredentialsDTO): Result<FirebaseUser?>

    /**
     * Signs up a user with their Google account.
     *
     * @param idToken The Google ID token obtained from the Google Sign-In process.
     * @return A [Result] object that encapsulates either:
     *         - The [FirebaseUser] object representing the signed-in user on success.
     *         - An [Exception] (typically [FirebaseAuthException]) on failure, detailing the error.
     *         Possible failures include invalid ID token, network issues, or Firebase authentication errors.
     */
    suspend fun signUpWithGoogle(idToken: String): Result<FirebaseUser?>

    /**
     * Fetches the current user's information.
     *
     * This function attempts to retrieve the currently signed-in Firebase user.
     * It uses a safe call mechanism to handle potential exceptions.
     *
     * @return A [Result] object containing either the [FirebaseUser] on success
     * or an [Exception] on failure.
     */
    suspend fun fetchUserInfo(): Result<FirebaseUser?>

    /**
     * Logs out the current user.
     *
     * @return A [Result] indicating success (with a success message) or failure (with an exception).
     */
    suspend fun logOut(): Result<String>
}


internal class AuthDataSourceImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthDataSource {

    private suspend fun <T> safeFirebaseAuthCall(
        operation: FirebaseAuthOperation,
        call: suspend () -> T
    ): Result<T> {
        return try {
            Result.success(call())
        } catch (exception: FirebaseAuthException) {
            Logger.e("AuthDataSource: ${operation.name}", exception.message ?: "Unknown error")
            Result.failure(exception)
        } catch (exception: Exception) {
            Logger.e("AuthDataSource: ${operation.name}", exception.message ?: "Unknown error")
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