package com.example.homebankfront.feature.accountrecovery.recoverypasswordinput

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.data.repositories.AccountRecoveryRepository
import com.example.homebankfront.feature.accountrecovery.recoverypasswordinput.RecoveryPasswordError.RecoveryPasswordFieldError
import com.example.homebankfront.feature.accountrecovery.recoverypasswordinput.RecoveryPasswordInputEvent.Authenticate
import com.example.homebankfront.feature.accountrecovery.recoverypasswordinput.RecoveryPasswordInputEvent.UpdatePasswordField
import com.example.homebankfront.feature.accountrecovery.recoverypasswordinput.RecoveryPasswordInputState.Authenticated
import com.example.homebankfront.feature.accountrecovery.recoverypasswordinput.RecoveryPasswordInputState.Input
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.*
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
class RecoveryPasswordInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recoveryRepository: AccountRecoveryRepository,
    private val networkErrorEmitter: EventEmitter<NetworkError>,
) : ViewModel() {
    private val emailAddress: String = checkNotNull(savedStateHandle["emailAddress"])

    private val _state: MutableStateFlow<RecoveryPasswordInputState> =
        MutableStateFlow(Input(emailAddress))
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<RecoveryPasswordError, Error>>(
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

    fun onEvent(event: RecoveryPasswordInputEvent) {
        when (event) {
            Authenticate -> authenticate()
            is UpdatePasswordField -> updatePasswordField(event.field)
        }
    }

    private fun authenticate() {
        val currentState = _state.value
        if (currentState !is Input) return

        when (val validationResult = currentState.validate()) {
            is Success -> {
                viewModelScope.launch {
                    when (val result = recoveryRepository.authenticate(currentState.toRequest())) {
                        is Success -> _state.update {
                            Authenticated(result.data.token)
                        }

                        is Failure -> {
                            TODO()
                        }
                    }
                }
            }

            is Failure -> _state.update { validationResult.error }
        }

    }

    private fun updatePasswordField(field: RecoveryPasswordField) = _state.update { currentState ->
        if (currentState !is Input) return else currentState.copy(recoveryPasswordField = field)
    }
}

sealed interface RecoveryPasswordInputState {
    data class Input(
        val emailAddress: String,
        val recoveryPasswordField: RecoveryPasswordField = RecoveryPasswordField(),
        val isLoading: Boolean = false
    ) : RecoveryPasswordInputState {
        fun validate(): ResultGeneric<Unit, Input> {
            val recoveryPasswordFieldError = recoveryPasswordField.validate()

            val errors = listOf(recoveryPasswordFieldError)

            val newState = copy(
                recoveryPasswordField = recoveryPasswordField.copy(error = recoveryPasswordFieldError)
            )

            return if (errors.any { it != null }) Failure(newState) else Success(Unit)
        }

        fun toRequest() = AuthenticationRequest(
            email = emailAddress,
            password = recoveryPasswordField.password
        )
    }

    data class Authenticated(val recoveryToken: String) : RecoveryPasswordInputState
}

sealed interface RecoveryPasswordInputEvent {
    data class UpdatePasswordField(val field: RecoveryPasswordField) : RecoveryPasswordInputEvent
    data object Authenticate : RecoveryPasswordInputEvent
}

data class RecoveryPasswordField(
    val password: String = "",
    val error: RecoveryPasswordFieldError? = null
) {
    fun validate(): RecoveryPasswordFieldError? =
        if (password.isBlank()) RecoveryPasswordFieldError.MissingPassword else null
}

sealed class RecoveryPasswordError(val stringResourceId: Int) {
    sealed class RecoveryPasswordFieldError(stringResourceId: Int) :
        RecoveryPasswordError(stringResourceId) {
        data object InvalidPassword : RecoveryPasswordFieldError(R.string.wrong_password)
        data object MissingPassword : RecoveryPasswordFieldError(R.string.missing_password)
    }

    @Composable
    fun toStringResource() = stringResource(stringResourceId)


    fun getStringResourceFromContext(context: Context) = context.getString(stringResourceId)
}