package com.example.homebankfront.feature.authentication

sealed interface AuthenticationEvent {
    data class UpdateField(val field: AuthenticationField) : AuthenticationEvent
    data object ToggleAutoAuthentication : AuthenticationEvent
    data object Authenticate : AuthenticationEvent
    data object NoCredentials : AuthenticationEvent
}

