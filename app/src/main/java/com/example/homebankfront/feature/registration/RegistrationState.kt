package com.example.homebankfront.feature.registration

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.bodies.RegistrationRequest
import com.example.homebankfront.feature.authentication.AuthenticationState
import com.example.homebankfront.feature.registration.RegistrationError.EmailFieldError
import com.example.homebankfront.feature.registration.RegistrationError.PasswordFieldError
import com.example.homebankfront.feature.registration.RegistrationError.UsernameFieldError
import com.example.homebankfront.feature.registration.RegistrationField.EmailField
import com.example.homebankfront.feature.registration.RegistrationField.PasswordField
import com.example.homebankfront.feature.registration.RegistrationField.UsernameField
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error

sealed interface RegistrationState {
    data class InProgress(
        val usernameField: UsernameField = UsernameField(),
        val passwordField: PasswordField = PasswordField(),
        val emailField: EmailField = EmailField(),
        val isLoading: Boolean = false
    ) : RegistrationState

    data class Success(
        val username: String = "",
        val password: String = "",
        val email: String = ""
    ) : RegistrationState

    data object Failure : RegistrationState
}

fun RegistrationState.InProgress.toRequest(): RegistrationRequest =
    RegistrationRequest(
        username = usernameField.username,
        password = passwordField.password,
        email = emailField.email
    )

sealed interface RegistrationEvent {
    data class UpdateField(val field: RegistrationField) : RegistrationEvent
    data object Register : RegistrationEvent
}

sealed interface RegistrationField {
    data class UsernameField(
        val username: String = "",
        val error: UsernameFieldError? = null
    ) : RegistrationField

    data class PasswordField(
        val password: String = "",
        val showPassword: Boolean = false,
        val error: PasswordFieldError? = null
    ) : RegistrationField

    data class EmailField(
        val email: String = "",
        val error: EmailFieldError? = null
    ) : RegistrationField
}

sealed class RegistrationError(val stringResourceId: Int) {
    sealed class UsernameFieldError(stringResourceId: Int) : RegistrationError(stringResourceId) {
        data object MissingUsername : UsernameFieldError(R.string.missing_username)
    }

    sealed class PasswordFieldError(stringResourceId: Int) : RegistrationError(stringResourceId) {
        data object MissingPassword : PasswordFieldError(R.string.missing_password)
    }

    sealed class EmailFieldError(stringResourceId: Int) : RegistrationError(stringResourceId) {
        data object MissingEmail : EmailFieldError(R.string.missing_email)
        data object InvalidEmail : EmailFieldError(R.string.invalid_email)
    }
}

fun String?.toRegistrationError(): Either<RegistrationError, Error> = when (this) {
    "InvalidEmail" -> Either.Left(EmailFieldError.InvalidEmail)
    else -> Either.Right(Error.UnknownError)
}

fun RegistrationError.getStringResourceFromContext(context: Context) = when (this) {
    EmailFieldError.InvalidEmail -> context.getString(stringResourceId)
    EmailFieldError.MissingEmail -> context.getString(stringResourceId)
    PasswordFieldError.MissingPassword -> context.getString(stringResourceId)
    UsernameFieldError.MissingUsername -> context.getString(stringResourceId)
}

@Composable
fun RegistrationError.toStringResource(): String = when (this) {
    EmailFieldError.InvalidEmail -> stringResource(stringResourceId)
    EmailFieldError.MissingEmail -> stringResource(stringResourceId)
    PasswordFieldError.MissingPassword -> stringResource(stringResourceId)
    UsernameFieldError.MissingUsername -> stringResource(stringResourceId)
}

fun RegistrationField.update(onEvent: (RegistrationEvent) -> Unit) =
    onEvent(RegistrationEvent.UpdateField(this))
