package com.example.homebankfront.feature.utility

sealed interface Result {
    data object Success : Result
    data class Failure(val message: String) : Result
}


sealed interface ResultGeneric<out T> {
    data object Success : ResultGeneric<Nothing>
    data class Failure<out T>(val error: T) : ResultGeneric<T>
}
