package com.example.homebankfront.data.bodies

data class ChangePasswordRequest(
    val refreshToken: String?,
    val oldPassword: String,
    val newPassword: String,
    val confirmNewPassword: String,
)