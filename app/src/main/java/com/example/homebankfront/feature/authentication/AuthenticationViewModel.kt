package com.example.homebankfront.feature.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.authentication.AuthenticationError.BadCredentials
import com.example.homebankfront.feature.authentication.AuthenticationEvent.*
import com.example.homebankfront.feature.authentication.AuthenticationField.PasswordField
import com.example.homebankfront.feature.authentication.AuthenticationField.EmailField
import com.example.homebankfront.feature.authentication.AuthenticationState.Authenticated
import com.example.homebankfront.feature.authentication.AuthenticationState.Authenticating
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.UnknownError
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
import com.example.homebankfront.feature.utility.logDebug
import com.example.homebankfront.feature.utility.logError
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
        MutableStateFlow(Authenticating())
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<AuthenticationError, Error>>(
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

    fun onEvent(event: AuthenticationEvent) {
        logDebug("Received event: $event")
        when (event) {
            is NoCredentials -> _state.update { Authenticating() }
            is Authenticate -> authenticate()
            is UpdateField -> updateField(event.field)
            is ToggleAutoAuthentication -> toggleAutoAuthentication()
        }
    }

    private fun updateField(field: AuthenticationField) = _state.update { currentState ->
        if (currentState !is Authenticating) return

        when (field) {
            is PasswordField -> currentState.copy(passwordField = field)
            is EmailField -> currentState.copy(emailField = field)
        }
    }

    private fun toggleAutoAuthentication() = _state.update { currentState ->
        if (currentState !is Authenticating) return else currentState.copy(autoAuthentication = !currentState.autoAuthentication)
    }

    private fun setLoading(isLoading: Boolean) = _state.update { currentState ->
        if (currentState !is Authenticating) return else currentState.copy(isLoading = isLoading)
    }

    private fun authenticate() {
        val currentState = _state.value
        if (currentState !is Authenticating) return

        when (val validationResult = currentState.validate()) {
            is Failure -> _state.update { validationResult.error }
            is Success -> {
                setLoading(true)
                viewModelScope.launch {
                    try {
                        when (val result = authRepository.authenticate(currentState.toRequest())) {
                            is Success -> _state.update {
                                Authenticated(
                                    email = currentState.emailField.email,
                                    password = currentState.passwordField.password,
                                    registerCredentials = !currentState.autoAuthentication
                                )
                            }

                            is Failure -> handleError(result.error)
                        }
                    } finally {
                        setLoading(false)
                    }
                }
            }
        }
    }

    private suspend fun handleError(error: Either<AuthenticationError, Error>) = when (error) {
        is Left -> when (error.value) {
            BadCredentials -> _errorFlow.emit(error)
            else -> _errorFlow.emit(Right(UnknownError))
        }

        is Right -> _errorFlow.emit(error)
    }
}
