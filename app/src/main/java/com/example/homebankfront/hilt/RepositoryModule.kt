package com.example.homebankfront.hilt

import com.example.homebankfront.dataAccess.ApiService
import com.example.homebankfront.dataAccess.repositories.CustomerRepository
import com.example.homebankfront.dataAccess.repositories.TransactionHeadRepository
import com.example.homebankfront.dataAccess.repositories.TransactionRowRepository
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
}