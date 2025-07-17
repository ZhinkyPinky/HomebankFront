package com.example.homebankfront.data.bodies

data class SetNewPasswordRequest(
    val recoveryToken: String,
    val newPassword: String,
    val confirmNewPassword: String
)
