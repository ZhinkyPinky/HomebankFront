package com.example.homebankfront.feature.utility

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventEmitter<T> {
    private val _event = MutableSharedFlow<T>()
    val event: SharedFlow<T> = _event.asSharedFlow()

    suspend fun emitEvent(event: T) {
        Logger.d(message = "$event")
        _event.emit(event)
    }
}