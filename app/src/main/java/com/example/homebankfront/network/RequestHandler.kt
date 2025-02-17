package com.example.homebankfront.network

import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.security.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class RequestHandler @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val networkErrorEmitter: EventEmitter<NetworkError>,
    private val errorHandler: HTTPErrorHandlerContext
) {
    private val excludedEndpoints = setOf(
        "/auth/login",
        "/auth/register",
        "/auth/refresh"
    )

    operator fun invoke(
        chain: Chain,
        retryCount: Int = 3
    ): Response {
        val originalRequest = chain.request()
        val modifiedRequest: Request
        val urlPath = originalRequest.url().encodedPath()

        Logger.d(message = "Handling request to: $urlPath")

        //Don't add access token when calling excluded endpoints.
        if (excludedEndpoints.any { urlPath.contains(it, ignoreCase = true) }) {
            Logger.d(message = "Endpoint: $urlPath is excluded. Will not attempt to add authorization header.")
            modifiedRequest = originalRequest
        } else {
            modifiedRequest = tokenStorage.getAccessToken()?.let {
                Logger.d(message = "Added authorization header for request to: ${originalRequest.url()}")
                originalRequest.addAuthorizationHeader(it)
            } ?: originalRequest
        }

        var lastException: Exception? = null
        for (i in 1..retryCount) {
            try {
                val response = chain.proceed(modifiedRequest)
                return if (!response.isSuccessful) {
                    Logger.e(message = "Request to: ${modifiedRequest.url()} failed with message code ${response.code()}.")
                    runBlocking {
                        errorHandler(originalRequest, chain, response.use { it.code() })
                    }
                } else {
                    Logger.d(message = "Request to: ${modifiedRequest.url()} was successful.")
                    response
                }
            } catch (e: SocketTimeoutException) {
                Logger.e(message = "Request to: ${modifiedRequest.url()} timed out. Attempt $i out of $retryCount.")
                if (i < retryCount) {
                    runBlocking {
                        networkErrorEmitter.emitEvent(NetworkError.ConnectionRetry(i, retryCount))
                    }
                }

                lastException = e
            } catch (e: IOException) {
                Logger.e(message = "Request to: ${modifiedRequest.url()} failed with ${e.message}. Attempt $i out of $retryCount.")
                lastException = e
            }
        }

        throw lastException ?: IOException("Request failed after $retryCount retries.")
    }
}

/**
 * Adds an authorization header with the specified token to the request.
 */
fun Request.addAuthorizationHeader(token: String): Request {
    return newBuilder()
        .addHeader("Authorization", "Bearer $token")
        .build()
}
