package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.RegistrationRequest
import com.example.homebankfront.data.bodies.SignOutRequest
import com.example.homebankfront.data.remote.services.AuthService
import com.example.homebankfront.feature.authentication.AuthenticationError
import com.example.homebankfront.feature.authentication.toAuthenticationError
import com.example.homebankfront.feature.registration.RegistrationError
import com.example.homebankfront.feature.registration.toRegistrationError
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.UnknownError
import com.example.homebankfront.feature.utility.NetworkError.SocketTimeOut
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
import com.example.homebankfront.feature.utility.logDebug
import com.example.homebankfront.feature.utility.logError
import com.example.homebankfront.security.TokenStorage
import java.net.SocketTimeoutException
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage,
    private val responseHandler: ResponseHandler,
) {
    suspend fun authenticate(authenticationRequest: AuthenticationRequest): ResultGeneric<String, Either<AuthenticationError, Error>> =
        runCatching {
            logDebug("Trying to authenticate user: ${authenticationRequest.email}")
            val response = authService.authenticate(authenticationRequest)
            responseHandler(
                response = response,
                onSuccess = { body ->
                    logDebug("Authentication successful for user: ${authenticationRequest.email}")

                    tokenStorage.saveAccessToken(body.accessToken)
                    body.refreshToken?.let { tokenStorage.saveRefreshToken(it) }

                    Success(body.accountStatus)
                },
                onFailure = { errorMessage: String? ->
                    logError("Authentication failed for user: ${authenticationRequest.email} with message $errorMessage")
                    Failure(errorMessage.toAuthenticationError())
                }
            )
        }.getOrElse { handleException(it) }

    suspend fun register(registrationRequest: RegistrationRequest): ResultGeneric<Unit, Either<RegistrationError, Error>> =
        runCatching {
            logDebug("Trying to register user: ${registrationRequest.email}")
            val response = authService.register(registrationRequest)
            responseHandler(
                response = response,
                onSuccess = { body ->
                    logDebug("Registration successful for user: ${registrationRequest.email}")
                    Success(Unit)
                },
                onFailure = { errorMessage: String? ->
                    logError("Registration failed for user: ${registrationRequest.email} with message $errorMessage")
                    Failure(errorMessage.toRegistrationError())
                })
        }.getOrElse { handleException(it) }

    suspend fun signOut() = runCatching {
        logDebug("Trying to sign out.")

        val refreshToken = tokenStorage.getRefreshToken()
        val signOutRequest = refreshToken?.let { SignOutRequest(it) }
        val response = signOutRequest?.let { authService.signOut(it) }

        tokenStorage.clearTokens()
        response?.let {
            responseHandler(
                response = response,
                onSuccess = {
                    logDebug("Sign out successful.")
                    Success(Unit)
                },
                onFailure = { errorMessage ->
                    logDebug("Sign out failed with message: $errorMessage")
                    Failure(Right(UnknownError))
                })
        }
    }.getOrElse { handleException(it) }

    suspend fun resendActivationEmail() = runCatching {
        logDebug("Requesting resend of activation email.")
        val response = authService.resendActivationEmail()
        responseHandler(
            response = response,
            onSuccess = {
                logDebug("Activation email resent successfully.")
                Success(Unit)
            },
            onFailure = { errorMessage ->
                logError("Failed to resend activation email with message: $errorMessage")
                Failure(Right(UnknownError))
            }
        )
    }.getOrElse { handleException(it) }

    private fun handleException(e: Throwable): Failure<Right<Error>> {
        e.message?.let { logError(it) }
        return when (e) {
            is SocketTimeoutException -> Failure(Right(SocketTimeOut))
            else -> Failure(Right(UnknownError))
        }
    }
}
