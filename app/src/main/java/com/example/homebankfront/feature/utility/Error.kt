package com.example.homebankfront.feature.utility

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.homebankfront.R


sealed class Error(val stringResourceId: Int) {
    data object UnknownError : Error(R.string.unknown_error)

    fun getStringResourceFromContext(context: Context): String = when (this) {
        is NetworkError.ConnectionRetry -> context.getString(
            stringResourceId,
            currentRetries,
            totalRetries
        )

        else -> context.getString(stringResourceId)
    }

    @Composable
    fun toStringResource(): String = when (this) {
        is NetworkError.ConnectionRetry -> stringResource(
            stringResourceId,
            currentRetries,
            totalRetries
        )

        else -> stringResource(stringResourceId)
    }
}


sealed class NetworkError(stringResourceId: Int) : Error(stringResourceId) {
    data object SocketTimeOut : NetworkError(R.string.connection_timed_out)

    data class ConnectionRetry(
        val currentRetries: Int,
        val totalRetries: Int
    ) : NetworkError(R.string.retry_connection)

    //TODO: HTTP-errorFlow?
    data object EmptyResponseBody : NetworkError(R.string.empty_response_body)
}
