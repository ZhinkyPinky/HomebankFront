package com.example.homebankfront.di

import android.content.Context
import com.example.homebankfront.security.SecureStorage
import com.example.homebankfront.security.SecureTokenStorage
import com.example.homebankfront.security.TokenStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SecurityModule {
    @Provides
    @Singleton
    fun provideSecureStorage(@ApplicationContext context: Context) = SecureStorage(context)

    @Provides
    @Singleton
    fun provideTokenStorage(secureStorage: SecureStorage): TokenStorage =
        SecureTokenStorage(secureStorage)
}