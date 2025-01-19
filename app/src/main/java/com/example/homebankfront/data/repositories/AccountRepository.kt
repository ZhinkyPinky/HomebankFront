package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.AuthenticationResponse
import com.example.homebankfront.data.bodies.Registration
import com.example.homebankfront.data.services.AuthService
import retrofit2.Response
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val authService: AuthService
) {
    suspend fun authenticate(authenticationRequest: AuthenticationRequest): Response<AuthenticationResponse> =
        authService.authenticate(authenticationRequest)

    suspend fun register(registration: Registration): Response<AuthenticationResponse> =
        authService.register(registration)
}