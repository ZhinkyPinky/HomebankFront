package com.example.homebankfront.data.repositories

import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.Registration
import com.example.homebankfront.data.bodies.AuthenticationResponse
import com.example.homebankfront.data.bodies.ErrorResponse
import com.example.homebankfront.data.remote.services.AuthService
import com.example.homebankfront.feature.utility.Result
import com.example.homebankfront.security.TokenStorage
import com.google.gson.Gson
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage
) : Repository() {
    suspend fun authenticate(authenticationRequest: AuthenticationRequest): Result {
        return runCatching {
            val response = authService.authenticate(authenticationRequest)
            handleResponse(
                response = response,
                onSuccess = { body ->
                    Logger.d(message = "Authentication successful for user: ${authenticationRequest.username}")
                    tokenStorage.saveAccessToken(body.accessToken)
                    tokenStorage.saveRefreshToken(body.refreshToken)
                }
            )
        }.getOrElse {
            when (it) {
                is SocketTimeoutException -> Result.Failure("Error: Connection timed out")
                else -> Result.Failure("Error: ${it.message ?: "Unknown error"}")
            }
        }
    }

    suspend fun register(registration: Registration): Result {
        return runCatching {
            val response = authService.register(registration)
            //TODO: Don't save tokens on registration?
            handleResponse(
                response = response,
                onSuccess = { body ->
                    Logger.d(message = "Registration successful for user: ${registration.username}")
                    tokenStorage.saveAccessToken(body.accessToken)
                    tokenStorage.saveRefreshToken(body.refreshToken)
                })
        }.getOrElse {
            Result.Failure("Error: ${it.message ?: "Unknown error"}")
        }
    }

    suspend fun logout() {
        tokenStorage.clearTokens()
        runCatching {
            authService.logout()
        }.onFailure { Logger.e(message = "Logout failed: ${it.message}") }
    }

}

open class Repository {
    inline fun <T> handleResponse(
        response: Response<T>,
        onSuccess: (T) -> Unit = {},
        onFailure: (T) -> Unit = {} //TODO: Different type.
    ): Result {
        Logger.d(message = "Boop")
        return when {
            response.isSuccessful -> {
                response.body()?.let {
                    onSuccess(it)
                    Result.Success
                } ?: Result.Failure("Error: Response body is empty")
            }

            else -> {
                val errorMessage = response.errorBody()?.use { body ->

                    Gson().fromJson(body.string(), ErrorResponse::class.java)?.error
                        ?: "Unknown error"
                } ?: "Unknown error"

                Result.Failure(errorMessage)
            }
        }
    }
}