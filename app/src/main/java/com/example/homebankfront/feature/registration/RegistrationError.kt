package com.example.homebankfront.feature.registration

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.homebankfront.R
import com.example.homebankfront.feature.registration.RegistrationError.EmailFieldError.InvalidEmail
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error

sealed class RegistrationError(val stringResourceId: Int) {
    sealed class EmailFieldError(stringResourceId: Int) : RegistrationError(stringResourceId) {
        data object MissingEmail : EmailFieldError(R.string.missing_email)
        data object InvalidEmail : EmailFieldError(R.string.invalid_email)
    }

    sealed class PasswordFieldError(stringResourceId: Int) : RegistrationError(stringResourceId) {
        data object MissingPassword : PasswordFieldError(R.string.missing_password)
    }

}

fun String?.toRegistrationError(): Either<RegistrationError, Error> = when (this) {
    "Invalid email" -> Either.Left(InvalidEmail)
    else -> Either.Right(Error.UnknownError)
}

fun RegistrationError.getStringResourceFromContext(context: Context) =
    context.getString(stringResourceId)

@Composable
fun RegistrationError.toStringResource(): String = stringResource(stringResourceId)