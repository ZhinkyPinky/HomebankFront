package com.example.homebankfront.feature.utility

sealed interface Result {
    data object Success : Result
    data class Failure(val message: String) : Result
}


sealed interface ResultGeneric<out S, out F> {
    data class Success<out S>(val data: S) : ResultGeneric<S, Nothing>
    data class Failure<out F>(val error: F) : ResultGeneric<Nothing, F>
}
