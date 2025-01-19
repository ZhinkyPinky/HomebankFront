package com.example.homebankfront.data.bodies

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String,
    val message: String,
)