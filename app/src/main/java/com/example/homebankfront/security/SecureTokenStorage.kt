package com.example.homebankfront.security

import javax.inject.Inject

class SecureTokenStorage @Inject constructor(private val secureStorage: SecureStorage) :
    TokenStorage {
    companion object {
        const val REFRESH_TOKEN_KEY = "refreshToken"
        const val ACCESS_TOKEN_KEY = "accessToken"
    }

    override fun saveAccessToken(accessToken: String) =
        secureStorage.saveString(ACCESS_TOKEN_KEY, accessToken)

    override fun getAccessToken(): String? = secureStorage.getString(ACCESS_TOKEN_KEY)

    override fun saveRefreshToken(refreshToken: String) =
        secureStorage.saveString(REFRESH_TOKEN_KEY, refreshToken)

    override fun getRefreshToken(): String? = secureStorage.getString(REFRESH_TOKEN_KEY)

    override fun clearTokens() = secureStorage.clear()
}