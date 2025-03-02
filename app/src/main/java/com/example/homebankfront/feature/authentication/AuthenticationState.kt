package com.example.homebankfront.feature.authentication

import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.feature.authentication.AuthenticationError.PasswordFieldError
import com.example.homebankfront.feature.authentication.AuthenticationError.PasswordFieldError.MissingPassword
import com.example.homebankfront.feature.authentication.AuthenticationError.UsernameFieldError
import com.example.homebankfront.feature.authentication.AuthenticationError.UsernameFieldError.MissingUsername
import com.example.homebankfront.feature.authentication.AuthenticationField.PasswordField
import com.example.homebankfront.feature.authentication.AuthenticationField.UsernameField
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success

sealed interface AuthenticationState {
    data object Authenticated : AuthenticationState

    data class Authenticating(
        val usernameField: UsernameField = UsernameField(),
        val passwordField: PasswordField = PasswordField(),
        val isLoading: Boolean = false
    ) : AuthenticationState {
        fun validate(): ResultGeneric<Unit, Authenticating> {
            val usernameFieldError = usernameField.validate()
            val passwordFieldError = passwordField.validate()

            val errors = listOf(usernameFieldError, passwordFieldError)

            val newState = copy(
                usernameField = usernameField.copy(error = usernameFieldError),
                passwordField = passwordField.copy(error = passwordFieldError)
            )

            return if (errors.any { it != null }) Failure(newState) else Success(Unit)
        }

        fun toRequest(): AuthenticationRequest = AuthenticationRequest(
            username = usernameField.username,
            password = passwordField.password
        )
    }
}

sealed interface AuthenticationField {
    data class UsernameField(
        val username: String = "",
        val error: UsernameFieldError? = null,
    ) : AuthenticationField {
        fun validate() = if (username.isBlank()) MissingUsername else null
    }

    data class PasswordField(
        val password: String = "",
        val showPassword: Boolean = false,
        val error: PasswordFieldError? = null
    ) : AuthenticationField {
        fun validate() = if (password.isBlank()) MissingPassword else null
        fun toggleVisibility() = copy(showPassword = !showPassword)
    }
}
