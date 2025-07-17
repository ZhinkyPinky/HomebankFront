package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.ChangePasswordRequest
import com.example.homebankfront.data.bodies.RecoveryRequest
import com.example.homebankfront.data.remote.services.UserService
import com.example.homebankfront.feature.changePassword.ChangePasswordError
import com.example.homebankfront.feature.accountrecovery.emailinput.RecoverUserAccountError
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.*
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.UnknownError
import com.example.homebankfront.feature.utility.NetworkError.SocketTimeOut
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
import com.example.homebankfront.feature.utility.logError
import java.net.SocketTimeoutException
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val userService: UserService,
    private val responseHandler: ResponseHandler,
) {
    suspend fun changePassword(request: ChangePasswordRequest): ResultGeneric<Unit, Either<ChangePasswordError, Error>> =
        runCatching {
            val response = userService.changePassword(request)

            responseHandler(
                response = response,
                onSuccess = { Success(Unit) },
                onFailure = { errorMessage: String? ->
                    //TODO: String to ChangePasswordError
                    ChangePasswordError.fromString(errorMessage)?.let {
                        Failure(Left(it))
                    } ?: Failure(Right(UnknownError))
                }
            )
        }.getOrElse { handleException(it) }

    private fun handleException(e: Throwable): Failure<Right<Error>> {
        logError(e.stackTraceToString())
        e.message?.let { logError(it) }
        return when (e) {
            is SocketTimeoutException -> Failure(Right(SocketTimeOut))
            else -> Failure(Right(UnknownError))
        }
    }
}