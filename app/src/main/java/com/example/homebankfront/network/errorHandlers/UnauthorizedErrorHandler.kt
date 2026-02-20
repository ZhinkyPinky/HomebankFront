package com.example.homebankfront.network.errorHandlers

import android.util.Log
import com.example.homebankfront.network.addAuthorizationHeader
import com.example.homebankfront.data.bodies.RefreshRequest
import com.example.homebankfront.data.remote.services.AuthService
import com.example.homebankfront.network.excludedEndpoints
import com.example.homebankfront.security.TokenStorage
import dagger.Lazy
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
 * Handles 401 Unauthorized errors by attempting to refresh the access token using the refresh token.
 * If the refresh is successful, it retries the original request with the new access token.
 * If the refresh fails, it proceeds with the original request, which will likely fail again with a 401.
 */
class UnauthorizedErrorHandler @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val authService: Lazy<AuthService>
) : ErrorHandler {
    private val refreshMutex = Mutex()

    override suspend fun handleError(request: Request, chain: Interceptor.Chain): Response {
        Log.d(this::class.simpleName, "Handling 401")
        if (!excludedEndpoints.contains(request.url.encodedPath)) {
            if (refresh()) {
                Log.d(this::class.simpleName, "Refresh successful")
                tokenStorage.getAccessToken()?.let {
                    Log.d(this::class.simpleName, "Token: $it")
                    val retryRequest = request.addAuthorizationHeader(it)
                    Log.d(this::class.simpleName, "RetryRequest: ${retryRequest.headers}")
                    return chain.proceed(retryRequest)
                }
            }

            Log.d(this::class.simpleName, "Refresh failed")
        }

        return chain.proceed(request)
    }

    private suspend fun refresh(): Boolean = refreshMutex.withLock {
        Log.d(this::class.simpleName, "Refresh entered")
        val refreshToken = tokenStorage.getRefreshToken()
        if (refreshToken.isNullOrBlank()) return false

        try {
            val response = authService.get().refresh(RefreshRequest(refreshToken))
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Log.d(this::class.simpleName, "Refresh token: ${body.refreshToken}")
                    Log.d(this::class.simpleName, "Access token: ${body.accessToken}")
                    tokenStorage.saveAccessToken(body.accessToken)
                    body.refreshToken?.let { tokenStorage.saveRefreshToken(it) }
                    true
                } ?: false
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e(this::class.simpleName, "Refresh: Failed to refresh token: ${e.message}")
            false
        }
    }
}