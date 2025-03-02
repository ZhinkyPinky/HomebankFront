package com.example.homebankfront

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.AppEvent.*
import com.example.homebankfront.MainActivityUiState.*
import com.example.homebankfront.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(Success)
    val state = _state.asStateFlow()

    fun onEvent(event: AppEvent) {
        when (event) {
            SignOut -> signOut()
        }
    }

    private fun signOut() = viewModelScope.launch { authRepository.signOut() }
}

sealed interface AppEvent {
    data object SignOut : AppEvent
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data object Success : MainActivityUiState
}
