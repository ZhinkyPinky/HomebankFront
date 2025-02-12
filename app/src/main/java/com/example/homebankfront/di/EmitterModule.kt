package com.example.homebankfront.di

import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.network.NetworkEvent
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
    fun provideNetworkEventEmitter(): EventEmitter<NetworkEvent> = EventEmitter()
}
