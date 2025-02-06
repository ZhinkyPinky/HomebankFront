package com.example.homebankfront.network

import com.example.homebankfront.Logger
import com.example.homebankfront.security.TokenStorage
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class RequestInterceptor @Inject constructor(private val requestHandler: RequestHandler) :
    Interceptor {
    override fun intercept(chain: Chain): Response {
        Logger.d(message = "Intercepted request to: ${chain.request().url()}")
        return requestHandler(chain)
    }
}

