package com.example.homebankfront.data.remote.services

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.RefreshRequest
import com.example.homebankfront.data.bodies.AuthenticationResponse
import com.example.homebankfront.data.bodies.Registration
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST(ApiPaths.LOGIN)
    suspend fun authenticate(@Body authenticationRequest: AuthenticationRequest): Response<AuthenticationResponse>

    @POST(ApiPaths.REGISTER)
    suspend fun register(@Body registration: Registration): Response<AuthenticationResponse>

    @POST(ApiPaths.REFRESH)
    suspend fun refresh(@Body refreshRequest: RefreshRequest): Response<AuthenticationResponse>

    @POST(ApiPaths.LOGOUT)
    suspend fun logout()
}