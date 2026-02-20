package com.example.homebankfront.data.repositories

import android.util.Log
import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.RecoveryRequest
import com.example.homebankfront.data.bodies.RecoveryTokenResponse
import com.example.homebankfront.data.bodies.SetNewPasswordRequest
import com.example.homebankfront.data.remote.services.AccountRecoveryService
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.UnknownError
import com.example.homebankfront.feature.utility.NetworkError.SocketTimeOut
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
import com.example.homebankfront.feature.utility.logError
import com.example.homebankfront.security.TokenStorage
import java.net.SocketTimeoutException

class AccountRecoveryRepository(
    private val accountRecoveryService: AccountRecoveryService,
    private val tokenStorage: TokenStorage,
    private val responseHandler: ResponseHandler
) {
    suspend fun initiateRecovery(recoveryRequest: RecoveryRequest): ResultGeneric<Unit, Either<Unit, Error>> =
        runCatching {
            Log.d("AccountRecoveryRepository", "Initiating account recovery for email: ${recoveryRequest.email}")
            val response = accountRecoveryService.initiateRecovery(recoveryRequest)

            responseHandler(
                response = response,
                onSuccess = { Success(Unit) },
                onFailure = { errorMessage: String? -> Failure(Right(UnknownError)) }
            )
        }.getOrElse { handleException(it) }

    suspend fun authenticate(authenticationRequest: AuthenticationRequest): ResultGeneric<RecoveryTokenResponse, Either<Unit, Error>> =
        runCatching {
            val response = accountRecoveryService.authenticateRecovery(authenticationRequest)

            responseHandler(
                response = response,
                onSuccess = { body ->
                    Log.d(
                        "AccountRecoveryRepository",
                        "Authentication successful for user: ${authenticationRequest.email} with token: $body"
                    )
                    Success(body)
                },
                onFailure = { errorMessage: String? -> Failure(Right(UnknownError)) }
            )
        }.getOrElse { handleException(it) }

    suspend fun setNewPassword(setNewPasswordRequest: SetNewPasswordRequest): ResultGeneric<Unit, Either<Unit, Error>> =
        runCatching {
            Log.i("AccountRecoveryRepository", "Setting new password.")
            val response = accountRecoveryService.setNewPassword(setNewPasswordRequest)
            responseHandler(
                response = response,
                onSuccess = { body ->

                    tokenStorage.saveAccessToken(body.accessToken)
                    body.refreshToken?.let { tokenStorage.saveRefreshToken(it) }

                    Success(Unit)
                },
                onFailure = { errorMessage: String? -> Failure(Right(UnknownError)) }
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
