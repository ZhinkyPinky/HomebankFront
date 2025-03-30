package com.example.homebankfront.feature.changePassword

import com.example.homebankfront.feature.changePassword.ChangePasswordField.PasswordField

sealed interface ChangePasswordEvent {
    data class UpdateOldPassword(val oldPassword: PasswordField) : ChangePasswordEvent
    data class UpdateNewPassword(val newPassword: PasswordField) : ChangePasswordEvent
    data class UpdateConfirmNewPassword(val confirmNewPassword: PasswordField) : ChangePasswordEvent
    data object ChangePassword : ChangePasswordEvent
}
