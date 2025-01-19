package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.Registration
import com.example.homebankfront.data.bodies.AuthenticationResponse
import com.example.homebankfront.data.services.AuthService
import com.example.homebankfront.feature.utility.Result
import com.example.homebankfront.security.TokenStorage
import retrofit2.Response
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage
) {
    suspend fun authenticate(username: String, password: String): Result {
        val requestBody = AuthenticationRequest(username, password)
        val response = authService.authenticate(requestBody)

        return handleResponse(response) { body ->
            tokenStorage.saveAccessToken(body.accessToken)
            tokenStorage.saveRefreshToken(body.refreshToken)
        }
    }

    suspend fun register(registration: Registration): Result {
        val response = authService.register(registration)

        return handleResponse(response) { body ->
            tokenStorage.saveAccessToken(body.accessToken)
            tokenStorage.saveRefreshToken(body.refreshToken)
        }
    }

    private fun handleResponse(
        response: Response<AuthenticationResponse>,
        onSuccess: (AuthenticationResponse) -> Unit
    ): Result {
        return if (response.isSuccessful) {
            response.body()?.let { body ->
                onSuccess(body)
                Result.Success
            } ?: Result.Failure("Error: Successful request, but the body is empty")
        } else {
            response.body()?.let { body ->
                Result.Failure(body.message)
            } ?: Result.Failure("Error: Failed request, and the body is empty")
        }
    }
}
