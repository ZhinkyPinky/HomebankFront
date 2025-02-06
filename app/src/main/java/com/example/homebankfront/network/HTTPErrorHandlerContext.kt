package com.example.homebankfront.network

import com.example.homebankfront.Logger
import com.example.homebankfront.network.errorHandlers.ErrorHandler
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class HTTPErrorHandlerContext @Inject constructor(
    private val errorHandlers: Map<Int, ErrorHandler>
) {
    suspend fun handleError(request: Request, chain: Chain, errorCode: Int): Response {
        Logger.d(message = "Handling error for request to: ${request.url()} with error code $errorCode")
        return errorHandlers[errorCode]?.handleError(request, chain) ?: chain.proceed(request)
    }
}