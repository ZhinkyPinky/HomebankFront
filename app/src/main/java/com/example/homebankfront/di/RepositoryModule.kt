package com.example.homebankfront.di

import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.data.remote.services.CustomerService
import com.example.homebankfront.data.repositories.CustomerRepository
import com.example.homebankfront.data.repositories.TransactionHeadRepository
import com.example.homebankfront.data.repositories.TransactionRowRepository
import com.example.homebankfront.data.remote.services.AuthService
import com.example.homebankfront.data.remote.services.TransactionHeadService
import com.example.homebankfront.data.remote.services.TransactionRowService
import com.example.homebankfront.data.repositories.ResponseHandler
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
    fun provideCustomerRepository(
        customerService: CustomerService,
        responseHandler: ResponseHandler
    ): CustomerRepository = CustomerRepository(customerService, responseHandler)

    @Provides
    @Singleton
    fun provideTransactionHeadRepository(
        transactionHeadService: TransactionHeadService,
        responseHandler: ResponseHandler
    ): TransactionHeadRepository =
        TransactionHeadRepository(transactionHeadService, responseHandler)

    @Provides
    @Singleton
    fun provideTransactionRowRepository(
        transactionRowService: TransactionRowService,
        responseHandler: ResponseHandler,
    ): TransactionRowRepository =
        TransactionRowRepository(transactionRowService, responseHandler)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthService,
        tokenStorage: TokenStorage,
        responseHandler: ResponseHandler,
    ): AuthRepository = AuthRepository(authService, tokenStorage, responseHandler)
}