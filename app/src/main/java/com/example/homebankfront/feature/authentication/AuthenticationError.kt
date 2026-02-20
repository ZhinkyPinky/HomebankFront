package com.example.homebankfront.feature.authentication

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.homebankfront.R
import com.example.homebankfront.feature.authentication.AuthenticationError.*
import com.example.homebankfront.feature.authentication.AuthenticationError.PasswordFieldError.*
import com.example.homebankfront.feature.authentication.AuthenticationError.EmailFieldError.*
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.*

sealed class AuthenticationError(val stringResourceId: Int) {
    data object BadCredentials : AuthenticationError(R.string.bad_credentials)

    data object AccountNotActivated : AuthenticationError(R.string.account_not_activated)

    data object ActivationTokenExpired : AuthenticationError(R.string.activation_token_expired)
    sealed class EmailFieldError(stringResourceId: Int) : AuthenticationError(stringResourceId) {
        data object MissingEmail : EmailFieldError(R.string.missing_email)
    }

    sealed class PasswordFieldError(stringResourceId: Int) : AuthenticationError(stringResourceId) {
        data object MissingPassword : PasswordFieldError(R.string.missing_password)
    }

    data object UserDisabled : AuthenticationError(R.string.user_disabled)

    fun getStringResourceFromContext(context: Context) = context.getString(stringResourceId)

    @Composable
    fun toStringResource(): String = when (this) {
        BadCredentials -> stringResource(stringResourceId)
        AccountNotActivated -> stringResource(stringResourceId)
        ActivationTokenExpired -> stringResource(stringResourceId)
        MissingPassword -> stringResource(stringResourceId)
        MissingEmail -> stringResource(stringResourceId)
        UserDisabled -> stringResource(stringResourceId)
    }
}

fun String?.toAuthenticationError(): Either<AuthenticationError, Error> = when (this) {
    "BAD_CREDENTIALS" -> Either.Left(BadCredentials)
    "ACCOUNT_NOT_ACTIVATED" -> Either.Left(AccountNotActivated)
    "ACTIVATION_TOKEN_EXPIRED" -> Either.Left(ActivationTokenExpired)
    "User is disabled" -> Either.Left(UserDisabled)
    else -> Either.Right(UnknownError)
}


