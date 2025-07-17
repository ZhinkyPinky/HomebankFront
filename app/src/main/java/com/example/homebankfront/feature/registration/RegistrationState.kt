package com.example.homebankfront.feature.registration

import com.example.homebankfront.data.bodies.RegistrationRequest
import com.example.homebankfront.feature.registration.RegistrationError.EmailFieldError
import com.example.homebankfront.feature.registration.RegistrationError.EmailFieldError.MissingEmail
import com.example.homebankfront.feature.registration.RegistrationError.PasswordFieldError
import com.example.homebankfront.feature.registration.RegistrationError.PasswordFieldError.MissingPassword
import com.example.homebankfront.feature.registration.RegistrationField.EmailField
import com.example.homebankfront.feature.registration.RegistrationField.PasswordField
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success

sealed interface RegistrationState {
    data class Input(
        val emailField: EmailField = EmailField(),
        val passwordField: PasswordField = PasswordField(),
        val isLoading: Boolean = false
    ) : RegistrationState {
        fun validate(): ResultGeneric<Unit, Input> {
            val emailFieldError = emailField.validate()
            val passwordFieldError = passwordField.validate()

            val errors = listOf(emailFieldError, passwordFieldError)

            val newState = copy(
                emailField = emailField.copy(error = emailFieldError),
                passwordField = passwordField.copy(error = passwordFieldError),
            )

            return if (errors.any { it != null }) Failure(newState) else Success(Unit)
        }

        fun toRegisteredState(): Registered = Registered(
            email = emailField.email,
            password = passwordField.password,
        )

        fun toRequest(): RegistrationRequest = RegistrationRequest(
            email = emailField.email,
            password = passwordField.password,
        )
    }

    data class Registered(
        val email: String = "",
        val password: String = "",
    ) : RegistrationState
}

sealed interface RegistrationEvent {
    data class UpdateField(val field: RegistrationField) : RegistrationEvent
    data object Register : RegistrationEvent
}

sealed interface RegistrationField {
    data class EmailField(
        val email: String = "",
        val error: EmailFieldError? = null
    ) : RegistrationField {
        fun validate() = if (email.isBlank()) MissingEmail else null
    }

    data class PasswordField(
        val password: String = "",
        val error: PasswordFieldError? = null
    ) : RegistrationField {
        fun validate() = if (password.isBlank()) MissingPassword else null
    }
}


fun RegistrationField.update(onEvent: (RegistrationEvent) -> Unit) =
    onEvent(RegistrationEvent.UpdateField(this))
