package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.AuthenticationResponse
import com.example.homebankfront.data.bodies.RegistrationRequest
import com.example.homebankfront.data.remote.services.AuthService
import com.example.homebankfront.feature.authentication.AuthenticationError
import com.example.homebankfront.feature.authentication.toAuthenticationError
import com.example.homebankfront.feature.registration.RegistrationError
import com.example.homebankfront.feature.registration.toRegistrationError
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.UnknownError
import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.feature.utility.NetworkError.SocketTimeOut
import com.example.homebankfront.feature.utility.ResultGeneric
import java.net.SocketTimeoutException
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val responseHandler: ResponseHandler,
) {
    suspend fun authenticate(
        authenticationRequest: AuthenticationRequest
    ): ResultGeneric<AuthenticationResponse, Either<AuthenticationError, Error>> {
        Logger.d(message = "Trying to authenticate user: ${authenticationRequest.username}")
        return runCatching {
            val response = authService.authenticate(authenticationRequest)
            responseHandler(response = response,
                            onSuccess = { body ->
                                Logger.d(message = "Authentication successful for user: ${authenticationRequest.username}")
                                ResultGeneric.Success(body)
                            },
                            onFailure = { errorMessage: String? ->
                                Logger.d(message = "Authentication failed for user: ${authenticationRequest.username} with message $errorMessage")
                                ResultGeneric.Failure(errorMessage.toAuthenticationError())
                            }
            )
        }.getOrElse {
            when (it) {
                is SocketTimeoutException -> ResultGeneric.Failure(Right(SocketTimeOut))
                else -> ResultGeneric.Failure(Right(UnknownError))
            }
        }
    }

    suspend fun register(registrationRequest: RegistrationRequest): ResultGeneric<AuthenticationResponse, Either<RegistrationError, Error>> {
        Logger.d(message = "Trying to register user: ${registrationRequest.username}")
        return runCatching {
            val response = authService.register(registrationRequest)
            //TODO: Don't save tokens on registration?
            responseHandler(
                response = response,
                onSuccess = { body ->
                    Logger.d(message = "Registration successful for user: ${registrationRequest.username}")
                    ResultGeneric.Success(body)
                },
                onFailure = { errorMessage: String? ->
                    Logger.d(message = "Registration failed for user: ${registrationRequest.username} with message $errorMessage")
                    ResultGeneric.Failure(errorMessage.toRegistrationError())
                })
        }.getOrElse {
            when (it) {
                is SocketTimeoutException -> ResultGeneric.Failure(Right(SocketTimeOut))
                else -> ResultGeneric.Failure(Right(UnknownError))
            }
        }
    }

    suspend fun logout() = runCatching {
        Logger.d(message = "Trying to log out.")
        val response = authService.logout()
        responseHandler(
            response = response,
            onSuccess = { body ->
                ResultGeneric.Success(body)
            },
            onFailure = {
                ResultGeneric.Failure(Right(UnknownError))
            })
    }.getOrElse {
        when (it) {
            is SocketTimeoutException -> ResultGeneric.Failure(Right(SocketTimeOut))
            else -> ResultGeneric.Failure(Right(UnknownError))
        }
    }
}
