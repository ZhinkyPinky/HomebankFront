package com.example.homebankfront.data.remote.services

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.RefreshRequest
import com.example.homebankfront.data.bodies.AccessAndRefreshTokenResponse
import com.example.homebankfront.data.bodies.RegistrationRequest
import com.example.homebankfront.data.bodies.SignOutRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST(ApiPaths.LOGIN)
    suspend fun authenticate(@Body authenticationRequest: AuthenticationRequest): Response<AccessAndRefreshTokenResponse>

    @POST(ApiPaths.REGISTER)
    suspend fun register(@Body registrationRequest: RegistrationRequest): Response<AccessAndRefreshTokenResponse>

    @POST(ApiPaths.REFRESH)
    suspend fun refresh(@Body refreshRequest: RefreshRequest): Response<AccessAndRefreshTokenResponse>

    @POST(ApiPaths.LOGOUT)
    suspend fun signOut(@Body signOutRequest: SignOutRequest): Response<Unit>
}