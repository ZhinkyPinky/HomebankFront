package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.ChangePasswordRequest
import com.example.homebankfront.data.bodies.RecoveryRequest
import com.example.homebankfront.data.remote.services.AccountRecoveryService

class AccountRecoveryRepository(
    private val userService: AccountRecoveryService,
    private val responseHandler: ResponseHandler
) {

    suspend fun initiateRecovery(recoveryRequest: RecoveryRequest): Result<Unit> {
        return responseHandler.handleResponse(
            response = userService.initiateRecovery(recoveryRequest),
            onSuccess = { Result.success(Unit) },
            onFailure = { errorMessage: String? ->
                // Handle the error message appropriately, e.g., log it or convert it to a specific error type
                Result.failure(Exception(errorMessage ?: "Unknown error"))
            }
        )
    }

    suspend fun setNewPassword(changePasswordRequest: ChangePasswordRequest): Result<Unit> {
        return responseHandler(
            response = userService.changePassword(changePasswordRequest),
            onSuccess = { Result.success(Unit) },
            onFailure = { errorMessage: String? ->
                ) {
                userService.changePassword(changePasswordRequest)
            }
            }
    }