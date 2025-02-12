package com.example.homebankfront.feature.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.bodies.Registration
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.registration.RegistrationEvent.*
import com.example.homebankfront.feature.registration.RegistrationState.*
import com.example.homebankfront.feature.registration.domain.RegistrationUseCase
import com.example.homebankfront.feature.utility.Event
import com.example.homebankfront.feature.utility.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _registrationState: MutableStateFlow<RegistrationState> =
        MutableStateFlow(InProgress())
    val registrationState = _registrationState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event<String>>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is Register -> register()
            is Update -> updateRegistrationDetails(event.field)
        }
    }

    private fun register() = viewModelScope.launch {
        _registrationState.value.let { state ->
            if (state is InProgress) {
                val result = registrationUseCase(
                    Registration(
                        username = state.username,
                        password = state.password,
                        email = state.email
                    )
                )

                when (result) {
                    is Result.Failure -> showSnackbar(result.message)
                    is Result.Success -> {
                        _registrationState.update {
                            Success(
                                state.username,
                                state.password,
                                state.email
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateRegistrationDetails(field: RegistrationField) {
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

    private fun showSnackbar(message: String) = viewModelScope.launch {
        _eventFlow.emit(Event(message))
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
    data class Username(
        val username: String
        //val error: String?
    ) : RegistrationField

    data class Password(val password: String) : RegistrationField
    data class Email(val email: String) : RegistrationField
}

fun RegistrationField.update(onEvent: (RegistrationEvent) -> Unit) =
    onEvent(Update(this))