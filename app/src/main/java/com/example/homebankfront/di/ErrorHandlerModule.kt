package com.example.homebankfront.di

import com.example.homebankfront.network.errorHandlers.ErrorHandler
import com.example.homebankfront.network.HTTPErrorHandlerContext
import com.example.homebankfront.network.errorHandlers.UnauthorizedErrorHandler
import com.example.homebankfront.data.remote.services.AuthService
import com.example.homebankfront.security.TokenStorage
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ErrorHandlerModule {
    //TODO: Change to bind??
    @Provides
    @Singleton
    fun provideHTTPErrorHandlerContext(errorHandlers: Map<Int,@JvmSuppressWildcards ErrorHandler>): HTTPErrorHandlerContext {
        return HTTPErrorHandlerContext(errorHandlers)
    }

    @Provides
    @Singleton
    fun provideErrorHandlers(
        unauthorizedErrorHandler: UnauthorizedErrorHandler
    ): Map<Int, ErrorHandler> {
        return mapOf(
            401 to unauthorizedErrorHandler
        )
    }

    @Provides
    @Singleton
    fun provideUnauthorizedErrorHandler(
        tokenStorage: TokenStorage,
        authService: Lazy<AuthService>
    ): UnauthorizedErrorHandler {
        return UnauthorizedErrorHandler(tokenStorage, authService)
    }
}