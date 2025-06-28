package com.example.homebankfront.data.bodies

data class AccessAndRefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val message: String,
)