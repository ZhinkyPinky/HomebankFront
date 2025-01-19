package com.example.homebankfront.feature.authentication

sealed interface AuthenticationEvent {
    data class UpdateUsername(val username: String) : AuthenticationEvent
    data class UpdatePassword(val password: String) : AuthenticationEvent
    data object Authenticate : AuthenticationEvent
    data object NoCredentials : AuthenticationEvent
}
