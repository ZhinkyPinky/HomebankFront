package com.example.homebankfront.feature.accountActivationPending

import com.example.homebankfront.feature.authentication.AuthenticationField

sealed interface AccountActivationPendingEvent {
    data object ResendActivationEmail : AccountActivationPendingEvent
}
