package com.example.homebankfront.hilt

import com.example.homebankfront.data.repositories.AccountRepository
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.data.services.ApiService
import com.example.homebankfront.data.repositories.CustomerRepository
import com.example.homebankfront.data.repositories.TransactionHeadRepository
import com.example.homebankfront.data.repositories.TransactionRowRepository
import com.example.homebankfront.data.services.AuthService
import com.example.homebankfront.security.SecureStorage
import com.example.homebankfront.security.TokenStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideCustomerRepository(apiService: ApiService): CustomerRepository =
        CustomerRepository(apiService)

    @Provides
    @Singleton
    fun provideTransactionHeadRepository(apiService: ApiService): TransactionHeadRepository =
        TransactionHeadRepository(apiService)

    @Provides
    @Singleton
    fun provideTransactionRowRepository(apiService: ApiService): TransactionRowRepository =
        TransactionRowRepository(apiService)

    @Provides
    @Singleton
    fun provideAccountRepository(authService: AuthService): AccountRepository =
        AccountRepository(authService)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthService,
        tokenStorage: TokenStorage
    ): AuthRepository =
        AuthRepository(authService, tokenStorage)
}