package com.example.homebankfront.network

sealed interface NetworkEvent {
    data class SocketTimeOut(val message: String) : NetworkEvent
}