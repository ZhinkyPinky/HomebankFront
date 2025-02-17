package com.example.homebankfront.feature.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.registration.RegistrationError.EmailFieldError.InvalidEmail
import com.example.homebankfront.feature.registration.RegistrationError.EmailFieldError.MissingEmail
import com.example.homebankfront.feature.registration.RegistrationError.PasswordFieldError.MissingPassword
import com.example.homebankfront.feature.registration.RegistrationError.UsernameFieldError.MissingUsername
import com.example.homebankfront.feature.registration.RegistrationEvent.Register
import com.example.homebankfront.feature.registration.RegistrationEvent.UpdateField
import com.example.homebankfront.feature.registration.RegistrationField.EmailField
import com.example.homebankfront.feature.registration.RegistrationField.PasswordField
import com.example.homebankfront.feature.registration.RegistrationField.UsernameField
import com.example.homebankfront.feature.registration.RegistrationState.InProgress
import com.example.homebankfront.feature.registration.RegistrationState.Success
import com.example.homebankfront.feature.registration.domain.RegistrationUseCase
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.UnknownError
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.feature.utility.ResultGeneric
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
    private val registrationUseCase: RegistrationUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state: MutableStateFlow<RegistrationState> =
        MutableStateFlow(InProgress())
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
        Logger.d(message = "Received event: $event")
        when (event) {
            is Register -> register()
            is UpdateField -> updateField(event.field)
        }
    }

    private fun toggleLoading() = _state.update { currentState ->
        if (currentState is InProgress) currentState.copy(isLoading = !currentState.isLoading) else currentState
    }

    private fun register() {
        if (validateRegistrationDetails()) {
            toggleLoading()
            viewModelScope.launch {
                Logger.d(message = "Launched coroutine for registration event.")
                _state.value.let { currentState ->
                    if (currentState is InProgress) {
                        when (val result = authRepository.register(currentState.toRequest())) {
                            is ResultGeneric.Failure -> when (val error = result.error) {
                                is Left -> when (error.value) {
                                    is InvalidEmail -> updateField(
                                        currentState.emailField.copy(
                                            error = error.value
                                        )
                                    )

                                    else -> _errorFlow.emit(Right(UnknownError))
                                }

                                is Right -> _errorFlow.emit(error)
                            }

                            ResultGeneric.Success -> _state.update {
                                Success(
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

    private fun validateRegistrationDetails(): Boolean {
        var result = true

        _state.update { currentState ->
            if (currentState is InProgress) {
                val usernameField = currentState.usernameField
                val passwordField = currentState.passwordField
                val emailField = currentState.emailField

                currentState.copy(
                    usernameField = usernameField.copy(
                        error = if (usernameField.username.isBlank()) {
                            result = false
                            MissingUsername
                        } else {
                            null
                        },
                    ),
                    passwordField = passwordField.copy(
                        error = if (passwordField.password.isBlank()) {
                            result = false
                            MissingPassword
                        } else {
                            null
                        }
                    ),
                    emailField = emailField.copy(
                        error = if (emailField.email.isBlank()) {
                            result = false
                            MissingEmail
                        } else {
                            null
                        }
                    )
                )
            } else {
                currentState
            }
        }

        return result
    }

    private fun updateField(field: RegistrationField) = _state.update { currentState ->
        if (currentState is InProgress) {
            when (field) {
                is EmailField -> currentState.copy(emailField = field)
                is PasswordField -> currentState.copy(passwordField = field)
                is UsernameField -> currentState.copy(usernameField = field)
            }
        } else {
            currentState
        }
    }
}
