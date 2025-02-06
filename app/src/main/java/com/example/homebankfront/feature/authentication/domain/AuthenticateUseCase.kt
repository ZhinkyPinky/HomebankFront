package com.example.homebankfront.feature.authentication.domain

import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.utility.Result
import javax.inject.Inject


class AuthenticateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result =
        authRepository.authenticate(username, password)
}
