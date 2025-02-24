package com.example.homebankfront.feature.registration

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.homebankfront.R
import com.example.homebankfront.feature.registration.RegistrationError.EmailFieldError.InvalidEmail
import com.example.homebankfront.feature.registration.RegistrationError.UsernameFieldError.TakenUsername
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error

sealed class RegistrationError(val stringResourceId: Int) {
    sealed class UsernameFieldError(stringResourceId: Int) : RegistrationError(stringResourceId) {
        data object MissingUsername : UsernameFieldError(R.string.missing_username)
        data object TakenUsername : UsernameFieldError(R.string.taken_username)
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
    "Invalid email" -> Either.Left(InvalidEmail)
    "Username already exists" -> Either.Left(TakenUsername)
    else -> Either.Right(Error.UnknownError)
}

fun RegistrationError.getStringResourceFromContext(context: Context) =
    context.getString(stringResourceId)

@Composable
fun RegistrationError.toStringResource(): String = stringResource(stringResourceId)