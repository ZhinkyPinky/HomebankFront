package com.example.homebankfront.feature.accountrecovery.newpasswordinput

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.SetNewPasswordRequest
import com.example.homebankfront.data.repositories.AccountRecoveryRepository
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputError.PasswordFieldError
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputError.PasswordFieldError.InvalidPassword
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputError.PasswordFieldError.MissingPassword
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputEvent.SetNewPassword
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputEvent.UpdateConfirmNewPasswordField
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputEvent.UpdateNewPasswordField
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputState.Input
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputState.NewPasswordSet
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
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
class NewPasswordInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val accountRecoveryRepository: AccountRecoveryRepository,
    private val networkErrorEmitter: EventEmitter<NetworkError>,
) : ViewModel() {
    private val recoveryToken: String = checkNotNull(savedStateHandle["recoveryToken"])

    private val _state: MutableStateFlow<NewPasswordInputState> =
        MutableStateFlow(Input(recoveryToken))
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<NewPasswordInputError, Error>>(
        extraBufferCapacity = 10
    )
    val errorFlow = _errorFlow.asSharedFlow()

    init {
        observeNetworkEvents()
    }

    private fun observeNetworkEvents() = networkErrorEmitter.event.map {
        Right(it)
    }.buffer(10).onEach {
        _errorFlow.emit(it)
    }.catch { e ->
        e.message?.let { Logger.e(message = it) }
    }.launchIn(viewModelScope)

    fun onEvent(event: NewPasswordInputEvent) {
        when (event) {
            is UpdateConfirmNewPasswordField -> updateConfirmNewPasswordField(event.field)
            is SetNewPassword -> setNewPassword()
            is UpdateNewPasswordField -> updateNewPassword(event.field)
        }
    }

    private fun setNewPassword() {
        val currentState = _state.value
        if (currentState !is Input) return

        Log.d("NewPasswordInputViewModel", "Validating new password input.")
        when (val validationResult = currentState.validate()) {
            is Success -> {
                Log.d("NewPasswordInputViewModel", "Setting new password.")
                viewModelScope.launch {
                    when (val result =
                        accountRecoveryRepository.setNewPassword(currentState.toRequest())) {
                        is Success -> _state.update { NewPasswordSet }

                        is Failure -> handleError(result.error)
                    }

                }
            }

            is Failure -> _state.update {
                Log.d("NewPasswordInputViewModel", "Validation failed: ${validationResult.error}")
                validationResult.error }
        }
    }

    private fun updateNewPassword(field: PasswordField) = _state.update { currentState ->
        if (currentState !is Input) return else currentState.copy(newPasswordField = field)
    }

    private fun updateConfirmNewPasswordField(field: PasswordField) =
        _state.update { currentState ->
            if (currentState !is Input) return else currentState.copy(confirmNewPasswordField = field)
        }

    //TODO: Handle errors properly
    private suspend fun handleError(error: Either<Unit, Error>) = when (error) {
        is Right -> _errorFlow.emit(Right(Error.UnknownError))
        is Left -> _errorFlow.emit(Left(InvalidPassword))
    }

}

sealed interface NewPasswordInputState {
    data class Input(
        val recoveryToken: String,
        val newPasswordField: PasswordField = PasswordField(),
        val confirmNewPasswordField: PasswordField = PasswordField(),
        val isLoading: Boolean = false
    ) : NewPasswordInputState {
        fun validate(): ResultGeneric<Unit, Input> {
            val newPasswordFieldError = newPasswordField.validate()
            val confirmNewPasswordFieldError = confirmNewPasswordField.validate()

            val errors = listOf(newPasswordFieldError, confirmNewPasswordFieldError)

            val newState = copy(
                newPasswordField = newPasswordField.copy(error = newPasswordFieldError),
                confirmNewPasswordField = confirmNewPasswordField.copy(error = confirmNewPasswordFieldError)
            )

            return if (errors.any { it != null }) Failure(newState) else Success(Unit)
        }

        fun toRequest(): SetNewPasswordRequest = SetNewPasswordRequest(
            recoveryToken = recoveryToken,
            newPassword = newPasswordField.value,
            confirmNewPassword = confirmNewPasswordField.value
        )
    }

    data object NewPasswordSet : NewPasswordInputState
}

sealed interface NewPasswordInputEvent {
    data class UpdateNewPasswordField(val field: PasswordField) : NewPasswordInputEvent
    data class UpdateConfirmNewPasswordField(val field: PasswordField) : NewPasswordInputEvent
    data object SetNewPassword : NewPasswordInputEvent
}

data class PasswordField(
    val value: String = "",
    val error: PasswordFieldError? = null
) {
    fun validate() = if (value.isBlank()) MissingPassword else null
}

sealed class NewPasswordInputError(val stringResourceId: Int) {
    sealed class PasswordFieldError(stringResourceId: Int) :
        NewPasswordInputError(stringResourceId) {
        data object InvalidPassword : PasswordFieldError(R.string.wrong_password)
        data object PasswordsDoNotMatch : NewPasswordInputError(R.string.password_does_not_match)
        data object MissingPassword : PasswordFieldError(R.string.missing_password)
    }

    @Composable
    fun toStringResource() = stringResource(stringResourceId)

    fun getStringResourceFromContext(context: Context): String = context.getString(stringResourceId)
}
