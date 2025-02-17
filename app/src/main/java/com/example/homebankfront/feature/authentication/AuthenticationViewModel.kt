package com.example.homebankfront.feature.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.authentication.AuthenticationError.BadCredentials
import com.example.homebankfront.feature.authentication.AuthenticationError.PasswordFieldError.MissingPassword
import com.example.homebankfront.feature.authentication.AuthenticationError.UsernameFieldError.MissingUsername
import com.example.homebankfront.feature.authentication.AuthenticationEvent.Authenticate
import com.example.homebankfront.feature.authentication.AuthenticationEvent.NoCredentials
import com.example.homebankfront.feature.authentication.AuthenticationEvent.TogglePasswordVisibility
import com.example.homebankfront.feature.authentication.AuthenticationEvent.UpdateField
import com.example.homebankfront.feature.authentication.AuthenticationField.PasswordField
import com.example.homebankfront.feature.authentication.AuthenticationField.UsernameField
import com.example.homebankfront.feature.authentication.AuthenticationState.Authenticated
import com.example.homebankfront.feature.authentication.AuthenticationState.NotSignedIn
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
class AuthenticationViewModel @Inject constructor(
    private val networkErrorEmitter: EventEmitter<NetworkError>,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state: MutableStateFlow<AuthenticationState> =
        MutableStateFlow(NotSignedIn())
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<AuthenticationError, Error>>(
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

    fun onEvent(event: AuthenticationEvent) {
        Logger.d(message = "Received event: $event")
        when (event) {
            is NoCredentials -> _state.update { NotSignedIn() }
            is Authenticate -> authenticate()
            is TogglePasswordVisibility -> togglePasswordVisibility()
            is UpdateField -> updateField(event.field)
        }
    }

    private fun updateField(field: AuthenticationField) = _state.update { currentState ->
        if (currentState is NotSignedIn) {
            when (field) {
                is PasswordField -> currentState.copy(passwordField = field)
                is UsernameField -> currentState.copy(usernameField = field)
            }
        } else {
            currentState
        }
    }

    private fun togglePasswordVisibility() = _state.value.let { currentState ->
        if (currentState is NotSignedIn) {
            val passwordField = currentState.passwordField
            updateField(passwordField.copy(showPassword = !passwordField.showPassword))
        }
    }

    private fun toggleLoading() = _state.update { currentState ->
        if (currentState is NotSignedIn) currentState.copy(isLoading = !currentState.isLoading) else currentState
    }

    private fun authenticate() {
        if (validateAuthenticationDetails()) {
            toggleLoading()
            viewModelScope.launch {
                Logger.d(message = "Launched coroutine for authentication event.")
                _state.update { currentState ->
                    if (currentState is NotSignedIn) {
                        when (val result = authRepository.authenticate(currentState.toRequest())) {
                            is Success -> Authenticated
                            is Failure -> {
                                when (val error = result.error) {
                                    is Left -> when (error.value) {
                                        BadCredentials -> _errorFlow.emit(error)
                                        else -> _errorFlow.emit(Right(UnknownError))
                                    }

                                    is Right -> _errorFlow.emit(error)
                                }
                                currentState.copy(isLoading = false)
                            }
                        }
                    } else {
                        currentState
                    }
                }
            }
        }
    }

    private fun validateAuthenticationDetails(): Boolean {
        var result = true

        _state.update { currentState ->
            if (currentState is NotSignedIn) {
                val usernameField = currentState.usernameField
                val passwordField = currentState.passwordField

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
                    )
                )
            } else {
                currentState
            }
        }

        return result
    }
}
