package com.example.homebankfront.di

import com.example.homebankfront.data.repositories.AccountRepository
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.data.remote.services.CustomerService
import com.example.homebankfront.data.repositories.CustomerRepository
import com.example.homebankfront.data.repositories.TransactionHeadRepository
import com.example.homebankfront.data.repositories.TransactionRowRepository
import com.example.homebankfront.data.remote.services.AuthService
import com.example.homebankfront.data.remote.services.TransactionHeadService
import com.example.homebankfront.data.remote.services.TransactionRowService
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
    fun provideCustomerRepository(customerService: CustomerService): CustomerRepository =
        CustomerRepository(customerService)

    @Provides
    @Singleton
    fun provideTransactionHeadRepository(transactionHeadService: TransactionHeadService): TransactionHeadRepository =
        TransactionHeadRepository(transactionHeadService)

    @Provides
    @Singleton
    fun provideTransactionRowRepository(transactionRowService: TransactionRowService): TransactionRowRepository =
        TransactionRowRepository(transactionRowService)

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