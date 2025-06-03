package com.example.homebankfront.feature.accountrecovery.newpasswordinput

import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputError.*
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputEvent.*
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputState.Input
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.R
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
class NewPasswordInputViewModel @Inject constructor(
    private val networkErrorEmitter: EventEmitter<NetworkError>,
) : ViewModel() {
    private val _state: MutableStateFlow<NewPasswordInputState> = MutableStateFlow(Input())
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<NewPasswordInputError, Error>>(
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

    fun onEvent(event: NewPasswordInputEvent) {
        when (event) {
            is UpdateConfirmNewPasswordField -> TODO()
             is Authenticate -> TODO()
            is UpdateNewPasswordField -> updateNewPassword(event.field)
        }
    }

    private fun updateNewPassword(field: PasswordField) = _state.update { currentState ->
        if (currentState !is Input) return else currentState.copy(newPasswordField = field)
    }

    private fun updateConfirmNewPasswordField(field: PasswordField) =
        _state.update { currentState ->
            if (currentState !is Input) return else currentState.copy(confirmNewPasswordField = field)
        }
}

sealed interface NewPasswordInputState {
    data class Input(
        val newPasswordField: PasswordField = PasswordField(),
        val confirmNewPasswordField: PasswordField = PasswordField(),
        val isLoading: Boolean = false
    ) : NewPasswordInputState

    data object Changed : NewPasswordInputState
}

sealed interface NewPasswordInputEvent {
    data class UpdateNewPasswordField(val field: PasswordField) : NewPasswordInputEvent
    data class UpdateConfirmNewPasswordField(val field: PasswordField) : NewPasswordInputEvent
    data object Authenticate : NewPasswordInputEvent
}

data class PasswordField(
    val value: String = "",
    val error: PasswordFieldError? = null
)

sealed class NewPasswordInputError(val stringResourceId: Int) {
    sealed class PasswordFieldError(stringResourceId: Int) :
        NewPasswordInputError(stringResourceId) {
        data object InvalidPassword : PasswordFieldError(R.string.wrong_password) {
            override fun getStringResourceFromContext(context: Context): String {
                TODO("Not yet implemented")
            }
        }
    }

    @Composable
    fun toStringResource() = stringResource(stringResourceId)
    abstract fun getStringResourceFromContext(context: Context): String
}
