package com.example.homebankfront.feature.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.repositories.AccountRepository
import com.example.homebankfront.feature.authentication.AuthenticationEvent.Authenticate
import com.example.homebankfront.feature.authentication.AuthenticationEvent.NoCredentials
import com.example.homebankfront.feature.authentication.AuthenticationEvent.UpdatePassword
import com.example.homebankfront.feature.authentication.AuthenticationEvent.UpdateUsername
import com.example.homebankfront.feature.authentication.AuthenticationState.NotSignedIn
import com.example.homebankfront.feature.authentication.domain.AuthenticateUseCase
import com.example.homebankfront.feature.utility.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticate: AuthenticateUseCase
) : ViewModel() {
    private val _authenticationState: MutableStateFlow<AuthenticationState> =
        MutableStateFlow(NotSignedIn())
    val authenticationState = _authenticationState.asStateFlow()

    fun onEvent(event: AuthenticationEvent) {
        when (event) {
            is NoCredentials -> _authenticationState.update { NotSignedIn() }
            is Authenticate -> viewModelScope.launch {
                _authenticationState.value.let { currentState ->
                    if (currentState is AuthenticationState.NotSignedIn) {
                        val result = authenticate(currentState.username, currentState.password)
                        when (result) {
                            is Result.Success -> _authenticationState.update { AuthenticationState.Authenticated }
                            is Result.Failure -> {}
                        }
                    }
                }

            }

            is UpdatePassword -> _authenticationState.update { currentState ->
                if (currentState is NotSignedIn) {
                    currentState.copy(password = event.password)
                } else {
                    currentState
                }
            }

            is UpdateUsername -> _authenticationState.update { currentState ->
                if (currentState is NotSignedIn) {
                    currentState.copy(username = event.username)
                } else {
                    currentState
                }
            }
        }
    }
}
