package com.example.homebankfront.di

import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.NetworkError
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EmitterModule {
    @Provides
    @Singleton
    fun provideNetworkErrorEmitter(): EventEmitter<NetworkError> = EventEmitter()

    @Provides
    @Singleton
    fun provideAppStatusEmitter(): EventEmitter<Unit> = EventEmitter()
}
