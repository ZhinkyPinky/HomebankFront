package com.example.homebankfront.feature.changePassword

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.homebankfront.R
import com.example.homebankfront.feature.changePassword.ChangePasswordError.PasswordFieldError.*
import com.example.homebankfront.feature.utility.Error


sealed class ChangePasswordError(private val stringResourceId: Int) {
    sealed class PasswordFieldError(stringResourceId: Int) : ChangePasswordError(stringResourceId) {
        data object MissingPasswordError : PasswordFieldError(R.string.missing_password)
        data object PasswordTooShortError : PasswordFieldError(R.string.password_too_short)
        data object PasswordDoesNotMatchError : PasswordFieldError(R.string.password_does_not_match)
        data object WrongPassword : PasswordFieldError(R.string.wrong_password)
    }

    fun getStringResourceFromContext(context: Context) = context.getString(stringResourceId)

    @Composable
    fun toStringResource(): String = stringResource(stringResourceId)

    companion object {
        fun fromString(string: String?) = when (string) {
            "Wrong password" -> WrongPassword
            "Password too short" -> PasswordTooShortError
            "Missing password" -> MissingPasswordError
            "Password does not match" -> PasswordDoesNotMatchError
            else -> null
        }
    }
}
