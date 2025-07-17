package com.example.homebankfront.data.remote.services

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.AccessAndRefreshTokenResponse
import com.example.homebankfront.data.bodies.ChangePasswordRequest
import com.example.homebankfront.data.bodies.RecoveryRequest
import com.example.homebankfront.data.bodies.RecoveryTokenResponse
import com.example.homebankfront.data.bodies.SetNewPasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountRecoveryService {
    @POST(ApiPaths.SET_NEW_PASSWORD)
    suspend fun setNewPassword(@Body setNewPasswordRequest: SetNewPasswordRequest): Response<AccessAndRefreshTokenResponse>

    @POST(ApiPaths.INITIATE_RECOVERY)
    suspend fun initiateRecovery(@Body recoveryRequest: RecoveryRequest): Response<Unit>

    @POST(ApiPaths.ACCOUNT_RECOVERY_AUTHENTICATE)
    suspend fun authenticateRecovery(@Body authenticationRequest: AuthenticationRequest): Response<RecoveryTokenResponse>
}
