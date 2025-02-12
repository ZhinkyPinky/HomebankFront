package com.example.homebankfront.feature.authentication.domain

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.utility.Result
import javax.inject.Inject


class AuthenticateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result {
        return when (val result = validateAuthenticationDetails(username, password)) {
            is Result.Failure -> result
            is Result.Success -> authRepository.authenticate(
                AuthenticationRequest(
                    username,
                    password
                )
            )
        }
    }

    private fun validateAuthenticationDetails(username: String, password: String): Result {
        if (username.isBlank()) {
            return Result.Failure("Användarnamn saknas")
        }

        if (password.isBlank()) {
            return Result.Failure("Lösenord saknas")
        }

        return Result.Success
    }
}
