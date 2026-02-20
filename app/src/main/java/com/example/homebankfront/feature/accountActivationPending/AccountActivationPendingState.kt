package com.example.homebankfront.feature.accountActivationPending

import com.example.homebankfront.feature.authentication.AuthenticationState

sealed interface AccountActivationPendingState {
    data class ActivationPending(
        val isLoading: Boolean = false,
    ) : AccountActivationPendingState

    data object NewActivationEmailSent : AccountActivationPendingState
}
