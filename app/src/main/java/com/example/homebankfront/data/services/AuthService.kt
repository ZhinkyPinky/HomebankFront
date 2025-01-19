package com.example.homebankfront.data.services

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.RefreshRequest
import com.example.homebankfront.data.bodies.AuthenticationResponse
import com.example.homebankfront.data.bodies.Registration
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun authenticate(@Body authenticationRequest: AuthenticationRequest): Response<AuthenticationResponse>

    @POST("auth/register")
    suspend fun register(@Body registration: Registration): Response<AuthenticationResponse>

    @POST("auth/refresh")
    fun refresh(@Body refreshRequest: RefreshRequest) : Response<AuthenticationResponse>
}