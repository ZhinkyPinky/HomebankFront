package com.example.homebankfront.feature.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.registration.RegistrationError.EmailFieldError.InvalidEmail
import com.example.homebankfront.feature.registration.RegistrationError.UsernameFieldError
import com.example.homebankfront.feature.registration.RegistrationError.UsernameFieldError.*
import com.example.homebankfront.feature.registration.RegistrationEvent.Register
import com.example.homebankfront.feature.registration.RegistrationEvent.UpdateField
import com.example.homebankfront.feature.registration.RegistrationField.EmailField
import com.example.homebankfront.feature.registration.RegistrationField.PasswordField
import com.example.homebankfront.feature.registration.RegistrationField.UsernameField
import com.example.homebankfront.feature.registration.RegistrationState.Registered
import com.example.homebankfront.feature.registration.RegistrationState.Registering
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.UnknownError
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
import com.example.homebankfront.feature.utility.logDebug
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
class RegistrationViewModel @Inject constructor(
    private val networkErrorEmitter: EventEmitter<NetworkError>,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state: MutableStateFlow<RegistrationState> =
        MutableStateFlow(Registering())
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

    fun onEvent(event: RegistrationEvent) {
        logDebug("Received event: $event")
        when (event) {
            is Register -> register()
            is UpdateField -> updateField(event.field)
        }
    }

    private fun toggleLoading() = _state.value.let { currentState ->
        if (currentState is Registering) _state.update { currentState.copy(isLoading = !currentState.isLoading) }
    }

    private fun register() = _state.value.let { currentState ->
        if (currentState is Registering) {
            when (val validationResult = currentState.validate()) {
                is Failure -> _state.update { validationResult.error }
                is Success -> {
                    toggleLoading()
                    viewModelScope.launch {
                        when (val result = authRepository.register(currentState.toRequest())) {
                            is Failure -> {
                                handleError(result.error)
                                toggleLoading()
                            }

                            is Success -> {
                                _state.update {
                                    Registered(
                                        currentState.usernameField.username,
                                        currentState.passwordField.password,
                                        currentState.emailField.email
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private fun updateField(field: RegistrationField) = _state.value.let { currentState ->
        if (currentState is Registering) {
            _state.update {
                when (field) {
                    is EmailField -> currentState.copy(emailField = field)
                    is PasswordField -> currentState.copy(passwordField = field)
                    is UsernameField -> currentState.copy(usernameField = field)
                }
            }
        }
    }

    private suspend fun handleError(error: Either<RegistrationError, Error>) = when (error) {
        is Left -> when (error.value) {
            is TakenUsername -> _state.value.let { currentState ->
                if (currentState is Registering) {
                    updateField(currentState.usernameField.copy(error = error.value))
                }
            }

            is InvalidEmail -> _state.value.let { currentState ->
                if (currentState is Registering) {
                    updateField(currentState.emailField.copy(error = error.value))
                }
            }

            else -> _errorFlow.emit(Right(UnknownError))
        }

        is Right -> _errorFlow.emit(error)
    }
}
