package com.example.homebankfront.feature.authentication

import android.content.Context
import android.util.Log
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.GetCredentialException
import com.example.homebankfront.data.bodies.AuthenticationRequest
import javax.inject.Inject

class AuthenticationManager @Inject constructor(
    private val activityContext: Context,
) {
    private val credentialManager = CredentialManager.create(activityContext)

    suspend fun register(username: String, password: String) {
        val createPasswordRequest = CreatePasswordRequest(
            id = username,
            password = password
        )

        credentialManager.createCredential(
            context = activityContext,
            request = createPasswordRequest
        )
    }

    suspend fun signIn(): AuthenticationRequest {
        var authenticationRequest: AuthenticationRequest = AuthenticationRequest("", "")

        try {
            val request = GetCredentialRequest(credentialOptions = listOf(GetPasswordOption()))

            val result = credentialManager.getCredential(
                context = activityContext,
                request = request
            )

            when (val credential = result.credential) {
                is PasswordCredential -> {
                    val username: String = credential.id
                    val password: String = credential.password
                    authenticationRequest = AuthenticationRequest(
                        username = credential.id,
                        password = credential.password
                    )
                }
            }

        } catch (e: GetCredentialException) {
            handleFailure(e)
        }

        return authenticationRequest
    }

    fun handleSignIn(result: GetCredentialResponse): AuthenticationRequest {
        val credential = result.credential

        when (credential) {
            is PasswordCredential -> {
                val username: String = credential.id
                val password: String = credential.password
                return AuthenticationRequest(
                    username = credential.id,
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