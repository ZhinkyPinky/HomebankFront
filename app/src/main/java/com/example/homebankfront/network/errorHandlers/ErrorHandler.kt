package com.example.homebankfront.network.errorHandlers

import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response

interface ErrorHandler {
    suspend fun handleError(request: Request, chain: Chain): Response
}