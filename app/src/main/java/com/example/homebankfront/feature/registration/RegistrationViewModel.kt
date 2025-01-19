package com.example.homebankfront.feature.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.bodies.Registration
import com.example.homebankfront.data.repositories.AccountRepository
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.registration.RegistrationState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _registrationState: MutableStateFlow<RegistrationState> =
        MutableStateFlow(InProgress())
    val registrationState = _registrationState.asStateFlow()

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.Register -> register()
            is RegistrationEvent.Update -> updateRegistrationDetails(event.field)
        }
    }

    private fun register() = viewModelScope.launch {
        _registrationState.value.let { state ->
            if (state is RegistrationState.InProgress) {
                authRepository.register(
                    Registration(
                        username = state.username,
                        password = state.password,
                        email = state.email
                    )
                )
            }
        }
    }

    private fun updateRegistrationDetails(field: RegistrationField) =
        _registrationState.update { currentState ->
            if (currentState is InProgress) {
                when (field) {
                    is RegistrationField.Email -> currentState.copy(email = field.email)
                    is RegistrationField.Password -> currentState.copy(password = field.password)
                    is RegistrationField.Username -> currentState.copy(username = field.username)
                }
            } else {
                currentState
            }
        }
}

sealed interface RegistrationState {
    data class InProgress(
        val username: String = "",
        val password: String = "",
        val email: String = ""
    ) : RegistrationState

    data class Success(
        val username: String = "",
        val password: String = "",
        val email: String = ""
    ) : RegistrationState

    data object Failure : RegistrationState
}

sealed interface RegistrationEvent {
    data class Update(val field: RegistrationField) : RegistrationEvent
    data object Register : RegistrationEvent
}

sealed interface RegistrationField {
    data class Username(val username: String) : RegistrationField
    data class Password(val password: String) : RegistrationField
    data class Email(val email: String) : RegistrationField
}

fun RegistrationField.update(onEvent: (RegistrationEvent) -> Unit) =
    onEvent(RegistrationEvent.Update(this))