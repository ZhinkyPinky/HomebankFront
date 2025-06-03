package com.example.homebankfront.feature.accountrecovery.onetimepasswordinput

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.R
import com.example.homebankfront.feature.accountrecovery.onetimepasswordinput.OneTimePasswordError.OneTimePasswordFieldError
import com.example.homebankfront.feature.accountrecovery.onetimepasswordinput.OneTimePasswordInputEvent.Authenticate
import com.example.homebankfront.feature.accountrecovery.onetimepasswordinput.OneTimePasswordInputEvent.UpdatePasswordField
import com.example.homebankfront.feature.accountrecovery.onetimepasswordinput.OneTimePasswordInputState.Authenticated
import com.example.homebankfront.feature.accountrecovery.onetimepasswordinput.OneTimePasswordInputState.Default
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.feature.utility.NetworkError
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
import javax.inject.Inject

@HiltViewModel
class OneTimePasswordInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val networkErrorEmitter: EventEmitter<NetworkError>,
) : ViewModel() {
    private val emailAddress: String = checkNotNull(savedStateHandle["emailAddress"])

    private val _state: MutableStateFlow<OneTimePasswordInputState> =
        MutableStateFlow(Default(emailAddress))
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<OneTimePasswordError, Error>>(
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

    fun onEvent(event: OneTimePasswordInputEvent) {
        when (event) {
            Authenticate -> _state.update { Authenticated }
            is UpdatePasswordField -> updatePasswordField(event.field)
        }
    }

    private fun updatePasswordField(field: OneTimePasswordField) = _state.update { currentState ->
        if (currentState !is Default) return else currentState.copy(oneTimePasswordField = field)
    }
}

sealed interface OneTimePasswordInputState {
    data class Default(
        val emailAddress: String,
        val oneTimePasswordField: OneTimePasswordField = OneTimePasswordField(),
        val isLoading: Boolean = false
    ) : OneTimePasswordInputState

    data object Authenticated : OneTimePasswordInputState
}

sealed interface OneTimePasswordInputEvent {
    data class UpdatePasswordField(val field: OneTimePasswordField) : OneTimePasswordInputEvent
    data object Authenticate : OneTimePasswordInputEvent
}

data class OneTimePasswordField(
    val value: String = "",
    val error: OneTimePasswordFieldError? = null
)

sealed class OneTimePasswordError(val stringResourceId: Int) {
    sealed class OneTimePasswordFieldError(stringResourceId: Int) :
        OneTimePasswordError(stringResourceId) {
        data object InvalidPassword : OneTimePasswordFieldError(R.string.wrong_password)
    }

    @Composable
    fun toStringResource() = stringResource(stringResourceId)


    fun getStringResourceFromContext(context: Context) = context.getString(stringResourceId)
}