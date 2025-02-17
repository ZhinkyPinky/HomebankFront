package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.ErrorResponse
import com.example.homebankfront.data.bodies.RegistrationRequest
import com.example.homebankfront.data.remote.services.AuthService
import com.example.homebankfront.feature.authentication.AuthenticationError
import com.example.homebankfront.feature.authentication.toAuthenticationError
import com.example.homebankfront.feature.registration.RegistrationError
import com.example.homebankfront.feature.registration.toRegistrationError
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.feature.utility.Result
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.security.TokenStorage
import com.google.gson.Gson
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage
) {
    suspend fun authenticate(authenticationRequest: AuthenticationRequest): ResultGeneric<Either<AuthenticationError, Error>> {
        Logger.d(message = "Trying to authenticate user: ${authenticationRequest.username}")
        return runCatching {
            val response = authService.authenticate(authenticationRequest)
            handleResponse(
                response = response,
                onSuccess = { body ->
                    Logger.d(message = "Authentication successful for user: ${authenticationRequest.username}")
                    tokenStorage.saveAccessToken(body.accessToken)
                    tokenStorage.saveRefreshToken(body.refreshToken)
                },
                onFailure = { errorMessage: String? ->
                    Logger.d(message = "Authentication failed for user: ${authenticationRequest.username} with message $errorMessage")
                    ResultGeneric.Failure(errorMessage.toAuthenticationError())
                }
            )
        }.getOrElse {
            when (it) {
                is SocketTimeoutException -> ResultGeneric.Failure(Either.Right(NetworkError.SocketTimeOut))
                else -> ResultGeneric.Failure(Either.Right(Error.UnknownError))
            }
        }
    }

    suspend fun register(registrationRequest: RegistrationRequest): ResultGeneric<Either<RegistrationError, Error>> {
        return runCatching {
            val response = authService.register(registrationRequest)
            //TODO: Don't save tokens on registration?
            handleResponse(
                response = response,
                onSuccess = { body ->
                    Logger.d(message = "Registration successful for user: ${registrationRequest.username}")
                    tokenStorage.saveAccessToken(body.accessToken)
                    tokenStorage.saveRefreshToken(body.refreshToken)
                },
                onFailure = { errorMessage: String? ->
                    Logger.d(message = "Registration failed for user: ${registrationRequest.username} with message $errorMessage")
                    ResultGeneric.Failure(errorMessage.toRegistrationError())
                })
        }.getOrElse {
            when (it) {
                is SocketTimeoutException -> ResultGeneric.Failure(Either.Right(NetworkError.SocketTimeOut))
                else -> ResultGeneric.Failure(Either.Right(Error.UnknownError))
            }
        }
    }

    suspend fun logout() {
        tokenStorage.clearTokens()
        runCatching {
            authService.logout()
        }.onFailure { Logger.e(message = "Logout failed: ${it.message}") }
    }

    private inline fun <T, L> handleResponse(
        response: Response<T>,
        onSuccess: (T) -> Unit,
        onFailure: (String?) -> ResultGeneric.Failure<Either<L, Error>>  //TODO: Different type.
    ): ResultGeneric<Either<L, Error>> {
        return when {
            response.isSuccessful -> {
                response.body()?.let {
                    onSuccess(it)
                    ResultGeneric.Success
                } ?: ResultGeneric.Failure(Either.Right(Error.UnknownError))
            }

            else -> {
                val errorMessage = response.errorBody()?.use { body ->
                    Gson().fromJson(body.string(), ErrorResponse::class.java)?.error
                }

                onFailure(errorMessage)
            }
        }
    }
}

open class Repository {
    inline fun <T> handleResponse(
        response: Response<T>,
        onSuccess: (T) -> Unit = {},
        onFailure: (T) -> Unit = {} //TODO: Different type.
    ): Result {
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
                        ?: "Unknown errorFlow"
                } ?: "Unknown errorFlow"

                Result.Failure(errorMessage)
            }
        }
    }

//    inline fun <T> handleResponseGeneric(
//        response: Response<T>,
//        onSuccess: (T) -> Unit = {},
//        onFailure: (T) -> Unit = {} //TODO: Different type.
//    ): ResultGeneric<Unit> {
//        return when {
//            response.isSuccessful -> {
//                response.body()?.let {
//                    onSuccess(it)
//                    ResultGeneric.Success
//                } ?: ResultGeneric.Failure<Error>(NetworkError.EmptyResponseBody)
//            }
//
//            else -> {
//                val errorMessage = response.errorBody()?.use { body ->
//                    Gson().fromJson(body.string(), ErrorResponse::class.java)?.errorFlow
//                        ?: "Unknown errorFlow"
//                } ?: "Unknown errorFlow"
//
//                val errorFlow = when (errorMessage) {
//                    "Bad Credential" -> AuthenticationError.BadCredentials
//                    else -> Error.UnknownError
//                }
//
//                ResultGeneric.Failure(errorFlow)
//            }
//        }
//    }
}