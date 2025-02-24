package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.AuthenticationRequest
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
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
import com.example.homebankfront.feature.utility.logDebug
import com.example.homebankfront.security.TokenStorage
import java.net.SocketTimeoutException
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage,
    private val responseHandler: ResponseHandler,
) {
    suspend fun authenticate(
        authenticationRequest: AuthenticationRequest
    ): ResultGeneric<Unit, Either<AuthenticationError, Error>> {
        logDebug("Trying to authenticate user: ${authenticationRequest.username}")
        return runCatching {
            val response = authService.authenticate(authenticationRequest)
            responseHandler(response = response,
                            onSuccess = { body ->
                                logDebug("Authentication successful for user: ${authenticationRequest.username}")
                                tokenStorage.saveAccessToken(body.accessToken)
                                tokenStorage.saveRefreshToken(body.refreshToken)
                                Success(Unit)
                            },
                            onFailure = { errorMessage: String? ->
                                logDebug("Authentication failed for user: ${authenticationRequest.username} with message $errorMessage")
                                Failure(errorMessage.toAuthenticationError())
                            }
            )
        }.getOrElse {
            when (it) {
                is SocketTimeoutException -> Failure(Right(SocketTimeOut))
                else -> Failure(Right(UnknownError))
            }
        }
    }

    suspend fun register(registrationRequest: RegistrationRequest): ResultGeneric<Unit, Either<RegistrationError, Error>> {
        logDebug("Trying to register user: ${registrationRequest.username}")
        return runCatching {
            val response = authService.register(registrationRequest)
            //TODO: Don't save tokens on registration?
            responseHandler(
                response = response,
                onSuccess = { body ->
                    logDebug("Registration successful for user: ${registrationRequest.username}")
                    tokenStorage.saveAccessToken(body.accessToken)
                    tokenStorage.saveRefreshToken(body.refreshToken)
                    Success(Unit)
                },
                onFailure = { errorMessage: String? ->
                    logDebug("Registration failed for user: ${registrationRequest.username} with message $errorMessage")
                    Failure(errorMessage.toRegistrationError())
                })
        }.getOrElse {
            when (it) {
                is SocketTimeoutException -> Failure(Right(SocketTimeOut))
                else -> Failure(Right(UnknownError))
            }
        }
    }

    suspend fun logout() = runCatching {
        Logger.d(message = "Trying to log out.")
        val response = authService.logout()
        responseHandler(
            response = response,
            onSuccess = { body ->
                Success(body)
            },
            onFailure = {
                Failure(Right(UnknownError))
            })
    }.getOrElse {
        when (it) {
            is SocketTimeoutException -> Failure(Right(SocketTimeOut))
            else -> Failure(Right(UnknownError))
        }
    }
}
