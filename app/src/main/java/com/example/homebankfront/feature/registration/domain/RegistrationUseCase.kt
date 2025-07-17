package com.example.homebankfront.feature.registration.domain

import com.example.homebankfront.data.bodies.RegistrationRequest
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.utility.Result
import com.example.homebankfront.feature.utility.Result.*
import com.example.homebankfront.feature.utility.ResultGeneric
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(registrationRequest: RegistrationRequest): Result {
        return when (val result = validateRegistrationDetails(registrationRequest)) {
            is Failure -> result
            is Success -> when (authRepository.register(registrationRequest)) {
                //TODO: FIX
                is ResultGeneric.Failure -> Failure("")
                is ResultGeneric.Success -> Success
            }
        }
    }

    private fun validateRegistrationDetails(registrationRequest: RegistrationRequest): Result {
        if (registrationRequest.email.isBlank()) return Failure("E-mail saknas")
        if (registrationRequest.password.isBlank()) return Failure("Lösenord saknas")

        return Success
    }
}
