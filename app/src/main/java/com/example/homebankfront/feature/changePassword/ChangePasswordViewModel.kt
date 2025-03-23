package com.example.homebankfront.feature.changePassword

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.ChangePasswordRequest
import com.example.homebankfront.data.repositories.UserRepository
import com.example.homebankfront.feature.authentication.AuthenticationError
import com.example.homebankfront.feature.authentication.AuthenticationError.BadCredentials
import com.example.homebankfront.feature.authentication.AuthenticationError.UsernameFieldError.MissingUsername
import com.example.homebankfront.feature.changePassword.ChangePasswordError.PasswordFieldError
import com.example.homebankfront.feature.changePassword.ChangePasswordError.PasswordFieldError.MissingPassword
import com.example.homebankfront.feature.changePassword.ChangePasswordField.PasswordField
import com.example.homebankfront.feature.changePassword.ChangePasswordState.*
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.UnknownError
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.*
import com.example.homebankfront.feature.utility.logError
import com.example.homebankfront.security.SecureTokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val networkErrorEmitter: EventEmitter<NetworkError>,
    private val secureTokenStorage: SecureTokenStorage,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state: MutableStateFlow<ChangePasswordState> = MutableStateFlow(Ready())
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<ChangePasswordError, Error>>(
        extraBufferCapacity = 10
    )
    val errorFlow = _errorFlow.asSharedFlow()

    init {
        observeNetworkErrors()
    }

    private fun observeNetworkErrors() = networkErrorEmitter.event.map {
        Right(it)
    }.buffer(10).onEach {
        _errorFlow.emit(it)
    }.catch { e ->
        e.message?.let { logError(it) }
    }.launchIn(viewModelScope)

    fun onEvent(event: ChangePasswordEvent) {
        when (event) {
            ChangePasswordEvent.ChangePassword -> changePassword()
            is ChangePasswordEvent.UpdateConfirmNewPassword -> updateConfirmNewPassword(event.confirmNewPassword)
            is ChangePasswordEvent.UpdateNewPassword -> updateNewPassword(event.newPassword)
            is ChangePasswordEvent.UpdateOldPassword -> updateOldPassword(event.oldPassword)
        }
    }

    private fun changePassword() {
        val currentState = _state.value
        if (currentState !is Ready) return

        when (val validationResult = currentState.validate()) {
            is Failure -> _state.update { validationResult.error }
            is Success -> viewModelScope.launch {
                val refreshToken = secureTokenStorage.getRefreshToken()
                when (val result =
                    userRepository.changePassword(currentState.toRequest(refreshToken))) {
                    is Failure -> handleError(result.error)
                    is Success -> _state.update { PasswordChanged }
                }
            }
        }

    }

    private fun updateOldPassword(oldPassword: PasswordField) {
        _state.update { currentState ->
            if (currentState !is Ready) return else currentState.copy(oldPasswordField = oldPassword)
        }
    }

    private fun updateNewPassword(newPassword: PasswordField) {
        _state.update { currentState ->
            if (currentState !is Ready) return else currentState.copy(newPasswordField = newPassword)
        }
    }

    private fun updateConfirmNewPassword(confirmNewPassword: PasswordField) {
        _state.update { currentState ->
            if (currentState !is Ready) return else currentState.copy(confirmNewPasswordField = confirmNewPassword)
        }
    }

    private suspend fun handleError(error: Either<ChangePasswordError, Error>) = when (error) {
        is Left -> _errorFlow.emit(error)
        is Right -> _errorFlow.emit(error)
    }
}

sealed interface ChangePasswordState {
    data class Ready(
        val oldPasswordField: PasswordField = PasswordField(),
        val newPasswordField: PasswordField = PasswordField(),
        val confirmNewPasswordField: PasswordField = PasswordField(),
        val isLoading: Boolean = false
    ) : ChangePasswordState {
        fun validate(): ResultGeneric<Unit, Ready> {
            val oldPasswordFieldError = oldPasswordField.validate()
            val newPasswordFieldError = newPasswordField.validate()
            val confirmNewPasswordFieldError = confirmNewPasswordField.validate()

            val errors = listOf(
                oldPasswordFieldError,
                newPasswordFieldError,
                confirmNewPasswordFieldError
            )

            val newState = copy(
                oldPasswordField = oldPasswordField.copy(error = oldPasswordFieldError),
                newPasswordField = newPasswordField.copy(error = newPasswordFieldError),
                confirmNewPasswordField = confirmNewPasswordField.copy(error = confirmNewPasswordFieldError),
            )

            return if (errors.any { it != null }) Failure(newState) else Success(Unit)
        }

        fun toRequest(refreshToken: String?) = ChangePasswordRequest(
            refreshToken = refreshToken,
            oldPassword = oldPasswordField.password,
            newPassword = newPasswordField.password,
            confirmNewPassword = confirmNewPasswordField.password
        )
    }

    data object PasswordChanged : ChangePasswordState
}

sealed interface ChangePasswordField {
    data class PasswordField(
        val password: String = "",
        val error: PasswordFieldError? = null
    ) : ChangePasswordField {
        fun validate() = if (password.isBlank()) MissingPassword else null
    }
}

sealed class ChangePasswordError(val stringResourceId: Int) {
    sealed class PasswordFieldError(stringResourceId: Int) : ChangePasswordError(stringResourceId) {
        data object MissingPassword : PasswordFieldError(R.string.missing_password)
        data object ShortPassword : PasswordFieldError(R.string.password_too_short)
    }

    fun getStringResourceFromContext(context: Context) = context.getString(stringResourceId)

    @Composable
    fun toStringResource(): String = stringResource(stringResourceId)
}

sealed interface ChangePasswordEvent {
    data class UpdateOldPassword(val oldPassword: PasswordField) : ChangePasswordEvent
    data class UpdateNewPassword(val newPassword: PasswordField) : ChangePasswordEvent
    data class UpdateConfirmNewPassword(val confirmNewPassword: PasswordField) : ChangePasswordEvent
    data object ChangePassword : ChangePasswordEvent
}