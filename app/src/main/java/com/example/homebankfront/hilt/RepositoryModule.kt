package com.example.homebankfront.hilt

import com.example.homebankfront.dataAccess.ApiService
import com.example.homebankfront.dataAccess.repositories.Repository
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
    fun provideCustomerRepository(apiService : ApiService) : Repository {
        return Repository(apiService)
    }
}