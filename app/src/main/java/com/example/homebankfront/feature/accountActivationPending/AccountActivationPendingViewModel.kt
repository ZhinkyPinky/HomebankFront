package com.example.homebankfront.feature.accountActivationPending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.feature.accountActivationPending.AccountActivationPendingError.*
import com.example.homebankfront.feature.accountActivationPending.AccountActivationPendingEvent.ResendActivationEmail
import com.example.homebankfront.feature.accountActivationPending.AccountActivationPendingState.ActivationPending
import com.example.homebankfront.feature.accountActivationPending.AccountActivationPendingState.NewActivationEmailSent
import com.example.homebankfront.feature.authentication.AuthenticationState.Authenticating
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
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
class AccountActivationPendingViewModel @Inject constructor(
    private val networkErrorEmitter: EventEmitter<NetworkError>,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state: MutableStateFlow<AccountActivationPendingState> =
        MutableStateFlow(ActivationPending())
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<AccountActivationPendingError, Error>>(
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

    fun onEvent(event: AccountActivationPendingEvent) {
        logDebug("Received event: $event")
        when (event) {
            is ResendActivationEmail -> resendActivationEmail()
        }
    }

    private fun setLoading(isLoading: Boolean) = _state.update { currentState ->
        if (currentState !is ActivationPending) return else currentState.copy(isLoading = isLoading)
    }

    private fun resendActivationEmail() {
        setLoading(true)
        viewModelScope.launch {
            try {
                when (val result = authRepository.resendActivationEmail()) {
                    is Success -> {
                        logDebug("Resend activation email successful")
                        _state.update { NewActivationEmailSent }
                    }

                    is Failure -> {
                        logError("Resend activation email failed: ${result.error}")
                        _errorFlow.emit(Left(ResendEmailFailed))
                    }
                }
            } finally {
                setLoading(false)
            }
        }
    }
}