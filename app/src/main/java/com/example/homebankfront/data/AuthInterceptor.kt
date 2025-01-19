package com.example.homebankfront.data

import com.example.homebankfront.data.bodies.RefreshRequest
import com.example.homebankfront.data.services.AuthService
import com.example.homebankfront.security.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val authService: AuthService
) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = tokenStorage.getAccessToken()
        if (accessToken.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }

        var modifiedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(modifiedRequest)
        if (response.code() == 401) {
            synchronized(this) {
                refresh()?.let { newRefreshToken ->
                    modifiedRequest = originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer $newRefreshToken")
                        .build()
                }

                return chain.proceed(modifiedRequest)
            }
        }

        return response
    }

    private fun refresh(): String? = tokenStorage.getRefreshToken()?.let { refreshToken ->
        val response = authService.refresh(RefreshRequest(refreshToken))
        when (response.isSuccessful) {
            true -> response.body()?.accessToken?.let { newAccessToken ->
                tokenStorage.saveAccessToken(newAccessToken)
                newAccessToken
            }

            false -> null
        }
    }
}