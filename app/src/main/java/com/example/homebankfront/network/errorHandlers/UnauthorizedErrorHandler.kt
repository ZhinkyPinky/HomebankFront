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
                    tokenStorage.saveRefreshToken(body.refreshToken)
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