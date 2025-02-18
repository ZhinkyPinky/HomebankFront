package com.example.homebankfront.feature.authentication

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.feature.authentication.AuthenticationError.BadCredentials
import com.example.homebankfront.feature.authentication.AuthenticationError.PasswordFieldError
import com.example.homebankfront.feature.authentication.AuthenticationError.UsernameFieldError
import com.example.homebankfront.feature.authentication.AuthenticationField.PasswordField
import com.example.homebankfront.feature.authentication.AuthenticationField.UsernameField
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error

sealed interface AuthenticationState {
    data object Authenticated : AuthenticationState
    data class NotSignedIn(
        val usernameField: UsernameField = UsernameField(),
        val passwordField: PasswordField = PasswordField(),
        val isLoading: Boolean = false
    ) : AuthenticationState
}

fun AuthenticationState.NotSignedIn.toRequest(): AuthenticationRequest =
    AuthenticationRequest(
        username = this.usernameField.username,
        password = this.passwordField.password
    )

sealed interface AuthenticationField {
    data class UsernameField(
        val username: String = "",
        val error: UsernameFieldError? = null,
    ) : AuthenticationField

    data class PasswordField(
        val password: String = "",
        val showPassword: Boolean = false,
        val error: PasswordFieldError? = null
    ) : AuthenticationField
}

sealed class AuthenticationError(val stringResourceId: Int) {
    data object BadCredentials : AuthenticationError(R.string.bad_credentials)

    sealed class UsernameFieldError(stringResourceId: Int) : AuthenticationError(stringResourceId) {
        data object MissingUsername : UsernameFieldError(R.string.missing_username)
    }

    sealed class PasswordFieldError(stringResourceId: Int) : AuthenticationError(stringResourceId) {
        data object MissingPassword : PasswordFieldError(R.string.missing_password)
    }
}

fun String?.toAuthenticationError(): Either<AuthenticationError, Error> = when (this) {
    "Bad credentials" -> Either.Left(BadCredentials)
    else -> Either.Right(Error.UnknownError)
}

fun AuthenticationError.getStringResourceFromContext(context: Context) = when (this) {
    BadCredentials -> context.getString(stringResourceId)
    PasswordFieldError.MissingPassword -> context.getString(stringResourceId)
    UsernameFieldError.MissingUsername -> context.getString(stringResourceId)
}

@Composable
fun AuthenticationError.toStringResource(): String = when (this) {
    BadCredentials -> stringResource(stringResourceId)
    PasswordFieldError.MissingPassword -> stringResource(stringResourceId)
    UsernameFieldError.MissingUsername -> stringResource(stringResourceId)
}

