package com.example.homebankfront.feature.authentication.domain

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.authentication.AuthenticationError
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.ResultGeneric
import javax.inject.Inject


//class AuthenticateUseCase @Inject constructor(
//    private val authRepository: AuthRepository
//) {
//    suspend operator fun invoke(
//        username: String,
//        value: String
//    ): ResultGeneric<> {
//        return when (val result = validateAuthenticationDetails(username, value)) {
//            is ResultGeneric.Failure -> ResultGeneric.Failure(Either.Left(result.errorFlow))
//            is ResultGeneric.Success ->  authRepository.authenticate(
//                AuthenticationRequest(
//                    username,
//                    value
//                )
//            )
//        }
//    }
//
//    private fun validateAuthenticationDetails(
//        username: String,
//        value: String
//    ): ResultGeneric<AuthenticationError> {
//
//        if (username.isBlank()) {
//            return ResultGeneric.Failure<AuthenticationError>(AuthenticationError.UsernameFieldError.MissingUsername)
//        }
//
//        if (value.isBlank()) {
//            return ResultGeneric.Failure<AuthenticationError>(AuthenticationError.PasswordFieldError.MissingPassword)
//        }
//
//        return ResultGeneric.Success
//    }
//}
