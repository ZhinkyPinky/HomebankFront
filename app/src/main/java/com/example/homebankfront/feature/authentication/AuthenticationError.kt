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

    sealed class EmailFieldError(stringResourceId: Int) : AuthenticationError(stringResourceId) {
        data object MissingEmail : EmailFieldError(R.string.missing_email)
    }

    sealed class PasswordFieldError(stringResourceId: Int) : AuthenticationError(stringResourceId) {
        data object MissingPassword : PasswordFieldError(R.string.missing_password)
    }

    fun getStringResourceFromContext(context: Context) = context.getString(stringResourceId)

    @Composable
    fun toStringResource(): String = when (this) {
        BadCredentials -> stringResource(stringResourceId)
        MissingPassword -> stringResource(stringResourceId)
        MissingEmail -> stringResource(stringResourceId)
    }
}

fun String?.toAuthenticationError(): Either<AuthenticationError, Error> = when (this) {
    "Bad credentials" -> Either.Left(BadCredentials)
    else -> Either.Right(UnknownError)
}


