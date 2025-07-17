package com.example.homebankfront.feature.authentication

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.feature.authentication.AuthenticationError.PasswordFieldError
import com.example.homebankfront.feature.authentication.AuthenticationError.PasswordFieldError.MissingPassword
import com.example.homebankfront.feature.authentication.AuthenticationError.EmailFieldError
import com.example.homebankfront.feature.authentication.AuthenticationError.EmailFieldError.MissingEmail
import com.example.homebankfront.feature.authentication.AuthenticationField.PasswordField
import com.example.homebankfront.feature.authentication.AuthenticationField.EmailField
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success

sealed interface AuthenticationState {
    data class Authenticated(
        val email: String,
        val password: String,
        val registerCredentials: Boolean = true
    ) : AuthenticationState

    data class Authenticating(
        val emailField: EmailField = EmailField(),
        val passwordField: PasswordField = PasswordField(),
        val isLoading: Boolean = false,
        val autoAuthentication: Boolean = false
    ) : AuthenticationState {
        fun validate(): ResultGeneric<Unit, Authenticating> {
            val emailFieldError = emailField.validate()
            val passwordFieldError = passwordField.validate()

            val errors = listOf(emailFieldError, passwordFieldError)

            val newState = copy(
                emailField = emailField.copy(error = emailFieldError),
                passwordField = passwordField.copy(error = passwordFieldError)
            )

            return if (errors.any { it != null }) Failure(newState) else Success(Unit)
        }

        fun toRequest(): AuthenticationRequest = AuthenticationRequest(
            email = emailField.email,
            password = passwordField.password
        )
    }
}

sealed interface AuthenticationField {
    data class EmailField(
        val email: String = "",
        val error: EmailFieldError? = null,
    ) : AuthenticationField {
        fun validate() = if (email.isBlank()) MissingEmail else null
    }

    data class PasswordField(
        val password: String = "",
        val error: PasswordFieldError? = null
    ) : AuthenticationField {
        fun validate() = if (password.isBlank()) MissingPassword else null
    }
}
