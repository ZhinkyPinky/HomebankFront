package com.example.homebankfront.feature.accountrecovery.emailinput

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.RecoveryRequest
import com.example.homebankfront.data.repositories.AccountRecoveryRepository
import com.example.homebankfront.data.repositories.UserRepository
import com.example.homebankfront.feature.accountrecovery.emailinput.RecoverUserAccountError.*
import com.example.homebankfront.feature.accountrecovery.emailinput.RecoverUserAccountError.EmailFieldError.*
import com.example.homebankfront.feature.accountrecovery.emailinput.AccountRecoveryEmailInputEvent.*
import com.example.homebankfront.feature.accountrecovery.emailinput.AccountRecoveryEmailInputField.*
import com.example.homebankfront.feature.accountrecovery.emailinput.AccountRecoveryEmailInputState.*
import com.example.homebankfront.feature.registration.RegistrationError
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.*
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
import com.example.homebankfront.feature.utility.className
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
class AccountRecoveryEmailInputViewModel @Inject constructor(
    private val networkErrorEmitter: EventEmitter<NetworkError>,
    private val userRepository: AccountRecoveryRepository
) : ViewModel() {
    private val _state: MutableStateFlow<AccountRecoveryEmailInputState> = MutableStateFlow(Input())
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<RegistrationError, Error>>(
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

    fun onEvent(event: AccountRecoveryEmailInputEvent) {
        when (event) {
            RequestRecoveryPassword -> requestRecoveryPassword()
            is Update -> updateEmail(event.emailField)
        }
    }

    /**
     * Updates the email field in the state if the current state is [Input].
     *
     * @param emailField The new email field to update the state with.
     */
    private fun updateEmail(emailField: EmailField) = _state.update { currentState ->
        if (currentState !is Input) return else currentState.copy(emailField = emailField)
    }

    private fun setLoading(isLoading: Boolean) = _state.update { currentState ->
        if (currentState !is Input) return else currentState.copy(isLoading = isLoading)
    }

    private fun requestRecoveryPassword() {
        val currentState = _state.value
        if (currentState !is Input) return

        when (val validationResult = currentState.validate()) {
            is Failure -> _state.update { validationResult.error }
            is Success -> {
                setLoading(true)
                viewModelScope.launch {
                    try {
                        val request = currentState.toRequest()
                        when (userRepository.initiateRecovery(request)) {
                            is Failure -> TODO()
                            is Success -> _state.update { RecoveryPasswordSent(request.email) }
                        }
                    } catch (e: Exception) {
                        e.message?.let {
                            Log.e(this@AccountRecoveryEmailInputViewModel.className, it)
                        }
                    } finally {
                        setLoading(false)
                    }
                }
            }
        }
    }
}

sealed interface AccountRecoveryEmailInputState {
    data class Input(
        val emailField: EmailField = EmailField(),
        val isLoading: Boolean = false
    ) : AccountRecoveryEmailInputState {
        fun validate(): ResultGeneric<Unit, Input> {
            val emailFieldError = emailField.validate()

            val errors = listOf(emailFieldError)

            val newState = copy(
                emailField = emailField.copy(error = emailFieldError)
            )


            return if (errors.any { it != null }) Failure(newState) else Success(Unit)
        }

        fun toRequest() = RecoveryRequest(email = emailField.email)
    }

    data class RecoveryPasswordSent(val email: String) : AccountRecoveryEmailInputState
}

sealed interface AccountRecoveryEmailInputField {
    data class EmailField(
        val email: String = "",
        val error: EmailFieldError? = null
    ) : AccountRecoveryEmailInputField {
        fun validate() = if (email.isBlank()) MissingEmail else null
    }
}

sealed class RecoverUserAccountError(val stringResourceId: Int) {
    sealed class EmailFieldError(stringResourceId: Int) :
        RecoverUserAccountError(stringResourceId) {
        data object MissingEmail : EmailFieldError(R.string.missing_email)
        data object InvalidEmail : EmailFieldError(R.string.invalid_email)
    }

    fun getStringResourceFromContext(context: Context) = context.getString(stringResourceId)

    @Composable
    fun toStringResource(): String = stringResource(stringResourceId)
}

fun RegistrationError.getStringResourceFromContext(context: Context) =
    context.getString(stringResourceId)

sealed interface AccountRecoveryEmailInputEvent {
    data object RequestRecoveryPassword : AccountRecoveryEmailInputEvent
    data class Update(val emailField: EmailField) : AccountRecoveryEmailInputEvent
}