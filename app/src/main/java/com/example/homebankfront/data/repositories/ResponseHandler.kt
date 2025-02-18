package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.ErrorResponse
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.NetworkError.EmptyResponseBody
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
import com.google.gson.Gson
import retrofit2.Response

class ResponseHandler {
    inline operator fun <T, L> invoke(
        response: Response<T>,
        onSuccess: (T) -> Success<T>,
        onFailure: (String?) -> Failure<Either<L, Error>>
    ): ResultGeneric<T, Either<L, Error>> = when {
        response.isSuccessful -> response.body()?.let { body ->
            onSuccess(body)
        } ?: Failure(Right(EmptyResponseBody))

        else -> {
            val errorMessage = parseError(response)
            onFailure(errorMessage)
        }
    }

    fun parseError(response: Response<*>) = response.errorBody()?.use { body ->
        Gson().fromJson(body.string(), ErrorResponse::class.java)?.error
    }
}

