package com.example.homebankfront.feature.registration

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.RegistrationRequest
import com.example.homebankfront.feature.registration.RegistrationError.EmailFieldError
import com.example.homebankfront.feature.registration.RegistrationError.EmailFieldError.InvalidEmail
import com.example.homebankfront.feature.registration.RegistrationError.EmailFieldError.MissingEmail
import com.example.homebankfront.feature.registration.RegistrationError.PasswordFieldError
import com.example.homebankfront.feature.registration.RegistrationError.PasswordFieldError.MissingPassword
import com.example.homebankfront.feature.registration.RegistrationError.UsernameFieldError
import com.example.homebankfront.feature.registration.RegistrationError.UsernameFieldError.MissingUsername
import com.example.homebankfront.feature.registration.RegistrationField.EmailField
import com.example.homebankfront.feature.registration.RegistrationField.PasswordField
import com.example.homebankfront.feature.registration.RegistrationField.UsernameField
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success

sealed interface RegistrationState {
    data class Registering(
        val usernameField: UsernameField = UsernameField(),
        val passwordField: PasswordField = PasswordField(),
        val emailField: EmailField = EmailField(),
        val isLoading: Boolean = false
    ) : RegistrationState {
        fun validate(): ResultGeneric<Unit, Registering> {
            val usernameFieldError = usernameField.validate()
            val passwordFieldError = passwordField.validate()
            val emailFieldError = emailField.validate()

            val errors = listOf(usernameFieldError, passwordFieldError, emailFieldError)

            val newState = copy(
                usernameField = usernameField.copy(error = usernameFieldError),
                passwordField = passwordField.copy(error = passwordFieldError),
                emailField = emailField.copy(error = emailFieldError)
            )

            return if (errors.any { it != null }) Failure(newState) else Success(Unit)
        }

        fun toRequest(): RegistrationRequest = RegistrationRequest(
            username = usernameField.username,
            password = passwordField.password,
            email = emailField.email
        )
    }

    data class Registered(
        val username: String = "",
        val password: String = "",
        val email: String = ""
    ) : RegistrationState
}

sealed interface RegistrationEvent {
    data class UpdateField(val field: RegistrationField) : RegistrationEvent
    data object Register : RegistrationEvent
}

sealed interface RegistrationField {
    data class UsernameField(
        val username: String = "",
        val error: UsernameFieldError? = null
    ) : RegistrationField {
        fun validate() = if (username.isBlank()) MissingUsername else null
    }

    data class PasswordField(
        val password: String = "",
        val showPassword: Boolean = false,
        val error: PasswordFieldError? = null
    ) : RegistrationField {
        fun validate() = if (password.isBlank()) MissingPassword else null
    }

    data class EmailField(
        val email: String = "",
        val error: EmailFieldError? = null
    ) : RegistrationField {
        fun validate() = if (email.isBlank()) MissingEmail else null
    }
}


fun RegistrationField.update(onEvent: (RegistrationEvent) -> Unit) =
    onEvent(RegistrationEvent.UpdateField(this))
