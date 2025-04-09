package com.datahiveorg.donetik.util

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.util.Keys.WEB_CLIENT_ID
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID.randomUUID

class GoogleSignHelper(private val activity: Context) {

    private val credentialManager = CredentialManager.create(activity)

    suspend fun getGoogleIdToken(isLogin: Boolean): DomainResponse<String> {
        return try {
            val hashedNonce = getHashedNonce()

            val signUpWithGoogleOption = GetSignInWithGoogleOption
                .Builder(serverClientId = WEB_CLIENT_ID)
                .setNonce(hashedNonce)
                .build()

            val signInWithGoogleOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(serverClientId = WEB_CLIENT_ID)
                .setAutoSelectEnabled(true)
                .setNonce(hashedNonce)
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .setCredentialOptions(listOf(if(isLogin)signInWithGoogleOption else signUpWithGoogleOption))
                .build()

            val result = credentialManager.getCredential(context = activity, request = request)

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(result.credential.data)

            when {
                googleIdTokenCredential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                    Logger.i("Sign up with google", googleIdTokenCredential.idToken)
                    DomainResponse.Success(googleIdTokenCredential.idToken)
                }

                else -> DomainResponse.Failure("Invalid credential type")
            }
        } catch (exception: GetCredentialCancellationException) {
            Logger.e("Credential Manager", exception.message.toString())
            DomainResponse.Failure(exception.message.toString())
        } catch (exception: GetCredentialException) {
            Logger.e("Credential Manager", exception.message.toString())
            DomainResponse.Failure(exception.message.toString())
        } catch (exception: Exception) {
            Logger.e("Credential Manager", exception.message.toString())
            DomainResponse.Failure("An unexpected error occurred, please try again")
        }
    }

    suspend fun clearCredentials() {
        try {
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
        } catch (exception: ClearCredentialException) {
            Logger.e("Log out", exception.message.toString())
        }
    }

    private fun getHashedNonce(): String {
        val randomId = randomUUID().toString()
        return try {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            val digest = messageDigest.digest(randomId.toByteArray())
            digest.joinToString("") { "%02x".format(it) }
        } catch (exception: NoSuchAlgorithmException) {
            Logger.e("Hashed nonce", exception.message.toString())
            randomId.replace("-", "")
        }
    }
}