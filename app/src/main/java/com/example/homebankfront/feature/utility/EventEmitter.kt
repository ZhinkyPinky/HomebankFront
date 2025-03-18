package com.example.homebankfront.feature.utility

import androidx.lifecycle.viewModelScope
import com.example.homebankfront.feature.utility.Either.Right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class EventEmitter<T> {
    private val _event = MutableSharedFlow<T>()
    val event: SharedFlow<T> = _event.asSharedFlow()

    suspend fun emitEvent(event: T) {
        Logger.d(message = "$event")
        _event.emit(event)
    }
}