package com.example.homebankfront.feature.authentication

import android.content.Context
import android.util.Log
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialNoCreateOptionException
import androidx.credentials.exceptions.GetCredentialException
import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.feature.utility.Logger
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class AuthenticationManager @Inject constructor(
    @ActivityContext private val activityContext: Context
) {
    private val credentialManager = CredentialManager.create(activityContext)

    suspend fun registerCredentials(email: String, password: String) {
        Logger.d(message = "Trying to register $email")

        val createPasswordRequest = CreatePasswordRequest(
            id = email,
            password = password
        )

        try {
            credentialManager.createCredential(
                context = activityContext,
                request = createPasswordRequest
            )
        } catch (e: CreateCredentialNoCreateOptionException) {
            Logger.e(message = "No credential provider available: ${e.message}")
        } catch (e: Exception) {
            Logger.e(message = "Failed to register $email: ${e.message}")
        }
    }

    suspend fun signIn(): AuthenticationRequest? {
        try {
            val request = GetCredentialRequest(credentialOptions = listOf(GetPasswordOption()))

            val result = credentialManager.getCredential(
                context = activityContext,
                request = request
            )

            when (val credential = result.credential) {
                is PasswordCredential -> {
                    val email: String = credential.id
                    val password: String = credential.password
                    return AuthenticationRequest(
                        email = credential.id,
                        password = credential.password
                    )
                }
            }

        } catch (e: GetCredentialException) {
            handleFailure(e)
        }

        return null
    }

    fun handleSignIn(result: GetCredentialResponse): AuthenticationRequest {
        when (val credential = result.credential) {
            is PasswordCredential -> {
                val email: String = credential.id
                val password: String = credential.password
                return AuthenticationRequest(
                    email = credential.id,
                    password = credential.password
                )
            }

            else -> {
                return AuthenticationRequest("", "")
            }
        }
    }

    fun handleFailure(e: GetCredentialException) {
        Log.e("Auth Failure", e.type)
    }
}