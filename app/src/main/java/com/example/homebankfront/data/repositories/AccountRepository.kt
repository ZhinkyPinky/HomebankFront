package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.AuthenticationResponse
import com.example.homebankfront.data.bodies.RegistrationRequest
import com.example.homebankfront.data.remote.services.AuthService
import retrofit2.Response
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val authService: AuthService
) {
    suspend fun authenticate(authenticationRequest: AuthenticationRequest): Response<AuthenticationResponse> =
        authService.authenticate(authenticationRequest)

    suspend fun register(registrationRequest: RegistrationRequest): Response<AuthenticationResponse> =
        authService.register(registrationRequest)
}