package com.example.homebankfront.feature.registration.domain

import com.example.homebankfront.data.bodies.Registration
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.utility.Result
import com.example.homebankfront.feature.utility.Result.*
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(registration: Registration): Result {
        return when (val result = validateRegistrationDetails(registration)) {
            is Failure -> result
            is Success -> authRepository.register(registration)
        }
    }

    private fun validateRegistrationDetails(registration: Registration): Result {
        if (registration.username.isBlank()) {
            return Failure("Användarnamn saknas")
        }

        if (registration.password.isBlank()) {
            return Failure("Lösenord saknas")
        }

        if (registration.email.isBlank()) {
            return Failure("E-mail saknas")
        }

        return Success
    }
}
