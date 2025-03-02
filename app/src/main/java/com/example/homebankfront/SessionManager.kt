package com.example.homebankfront

import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.security.SecureTokenStorage
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val tokenStorage: SecureTokenStorage,
    private val emitter: EventEmitter<SessionEvent>
) {
    fun logOut() {
        tokenStorage.clearTokens()
        runBlocking { emitter.emitEvent(SessionEvent.SignOut) }
    }
}

sealed class SessionEvent() {
    data object SignOut : SessionEvent()
}