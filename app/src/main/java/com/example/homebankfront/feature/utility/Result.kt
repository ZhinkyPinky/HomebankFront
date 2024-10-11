package com.example.homebankfront.feature.utility

sealed interface Result {
    data object Success : Result
    data class Failure(val message: String) : Result
}