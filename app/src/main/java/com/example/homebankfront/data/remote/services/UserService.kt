package com.example.homebankfront.data.remote.services

import com.example.homebankfront.data.bodies.ChangePasswordRequest
import com.example.homebankfront.data.bodies.RecoveryRequest
import com.example.homebankfront.data.repositories.ResponseHandler
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @POST(ApiPaths.CHANGE_PASSWORD)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<Unit>

    @POST(ApiPaths.INITIATE_RECOVERY)
    suspend fun initiateRecovery(@Body recoveryRequest: RecoveryRequest): Response<Unit>
}