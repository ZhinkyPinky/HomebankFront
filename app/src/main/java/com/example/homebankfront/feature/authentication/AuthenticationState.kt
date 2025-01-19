package com.example.homebankfront.feature.authentication

sealed interface AuthenticationState {
    data object Authenticated : AuthenticationState
    data class NotSignedIn(
        val username: String = "",
        val password: String = ""
    ) : AuthenticationState
}