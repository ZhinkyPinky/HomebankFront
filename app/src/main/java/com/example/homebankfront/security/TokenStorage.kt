package com.example.homebankfront.security

import com.example.homebankfront.security.SecureTokenStorage.Companion.ACCESS_TOKEN_KEY
import com.example.homebankfront.security.SecureTokenStorage.Companion.REFRESH_TOKEN_KEY

interface TokenStorage {
    fun saveAccessToken(accessToken: String)
    fun getAccessToken(): String?
    fun saveRefreshToken(refreshToken: String)
    fun getRefreshToken(): String?
    fun clearTokens()
}