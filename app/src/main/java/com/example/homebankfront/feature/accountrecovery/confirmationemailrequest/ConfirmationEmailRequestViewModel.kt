package com.example.homebankfront.feature.accountrecovery.confirmationemailrequest

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.RecoveryRequest
import com.example.homebankfront.data.repositories.UserRepository
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.RecoverUserAccountError.*
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.RecoverUserAccountError.EmailFieldError.*
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.ConfirmationEmailRequestEvent.*
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.ConfirmationEmailRequestField.*
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.ConfirmationEmailRequestState.*
import com.example.homebankfront.feature.registration.RegistrationError
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Right
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
class ConfirmationEmailRequestViewModel @Inject constructor(
    private val networkErrorEmitter: EventEmitter<NetworkError>,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state: MutableStateFlow<ConfirmationEmailRequestState> = MutableStateFlow(Ready())
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

    fun onEvent(event: ConfirmationEmailRequestEvent) {
        when (event) {
            InitiateRecovery -> initiateRecovery()
            is Update -> updateEmail(event.emailField)
        }
    }

    private fun updateEmail(emailField: EmailField) = _state.update { currentState ->
        if (currentState !is Ready) return
        currentState.copy(emailField = emailField)
    }

    private fun setLoading(isLoading: Boolean) = _state.update { currentState ->
        if (currentState !is Ready) return else currentState.copy(isLoading = isLoading)
    }

    private fun initiateRecovery() {
        val currentState = _state.value
        if (currentState !is Ready) return

        when (val validationResult = currentState.validate()) {
            is Failure -> _state.update { validationResult.error }
            is Success -> {
                setLoading(true)
                viewModelScope.launch {
                    try {
                        when (val result =
                            userRepository.initiateRecovery(currentState.toRequest())) {
                            is Failure -> {}
                            is Success -> _state.update { RecoveryInitiated }
                        }
                    } catch (e: Exception) {
                        e.message?.let { Log.e(this@ConfirmationEmailRequestViewModel.className, it) }
                    } finally {
                        setLoading(false)
                    }
                }
            }
        }
    }
}

sealed interface ConfirmationEmailRequestState {
    data class Ready(
        val emailField: EmailField = EmailField(),
        val isLoading: Boolean = false
    ) : ConfirmationEmailRequestState {
        fun validate(): ResultGeneric<Unit, Ready> {
            val emailFieldError = emailField.validate()

            val errors = listOf(emailFieldError)

            val newState = copy(
                emailField = emailField.copy(error = emailFieldError)
            )


            return if (errors.any { it != null }) Failure(newState) else Success(Unit)
        }

        fun toRequest() = RecoveryRequest(email = emailField.email)
    }

    data object RecoveryInitiated : ConfirmationEmailRequestState
}

sealed interface ConfirmationEmailRequestField {
    data class EmailField(
        val email: String = "",
        val error: EmailFieldError? = null
    ) : ConfirmationEmailRequestField {
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

sealed interface ConfirmationEmailRequestEvent {
    data object InitiateRecovery : ConfirmationEmailRequestEvent
    data class Update(val emailField: EmailField) : ConfirmationEmailRequestEvent
}