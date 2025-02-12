package com.example.homebankfront.feature.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.feature.authentication.AuthenticationEvent.Authenticate
import com.example.homebankfront.feature.authentication.AuthenticationEvent.NoCredentials
import com.example.homebankfront.feature.authentication.AuthenticationEvent.UpdatePassword
import com.example.homebankfront.feature.authentication.AuthenticationEvent.UpdateUsername
import com.example.homebankfront.feature.authentication.AuthenticationState.NotSignedIn
import com.example.homebankfront.feature.authentication.domain.AuthenticateUseCase
import com.example.homebankfront.feature.utility.Event
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.Result
import com.example.homebankfront.network.NetworkEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val eventEmitter: EventEmitter<NetworkEvent>,
    private val authenticateUseCase: AuthenticateUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<AuthenticationState> =
        MutableStateFlow(NotSignedIn())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event<String>>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        observeNetworkEvents()
    }

    private fun observeNetworkEvents() = viewModelScope.launch {
        eventEmitter.event.collect {
            when (it) {
                is NetworkEvent.SocketTimeOut -> showSnackbar(it.message)
            }
        }
    }

    fun onEvent(event: AuthenticationEvent) {
        when (event) {
            is NoCredentials -> _uiState.update { NotSignedIn() }
            is Authenticate -> authenticate()

            is UpdatePassword -> _uiState.update { currentState ->
                if (currentState is NotSignedIn) {
                    currentState.copy(password = event.password)
                } else {
                    currentState
                }
            }

            is UpdateUsername -> _uiState.update { currentState ->
                if (currentState is NotSignedIn) {
                    currentState.copy(username = event.username)
                } else {
                    currentState
                }
            }
        }
    }

    private fun authenticate() = viewModelScope.launch {
        _uiState.update { currentState ->
            if (currentState is AuthenticationState.NotSignedIn) {
                currentState.copy(isWaiting = true)
            } else {
                currentState
            }
        }

        _uiState.update { currentState ->
            if (currentState is AuthenticationState.NotSignedIn) {
                val result = authenticateUseCase(currentState.username, currentState.password)
                when (result) {
                    is Result.Success -> AuthenticationState.Authenticated
                    is Result.Failure -> {
                        showSnackbar(result.message)
                        currentState.copy(isWaiting = false)
                    }
                }
            } else {
                currentState
            }
        }
    }

    private fun showSnackbar(message: String) = viewModelScope.launch {
        _eventFlow.emit(Event(message))
    }
}
