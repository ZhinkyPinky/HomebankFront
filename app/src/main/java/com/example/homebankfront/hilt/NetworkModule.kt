package com.example.homebankfront.hilt

import com.example.homebankfront.data.services.ApiService
import com.example.homebankfront.data.LocalDateAdapter
import com.example.homebankfront.data.LocalDateTimeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetroFit() : Retrofit {
        val gson : Gson = GsonBuilder()
            .registerTypeAdapter(
                LocalDateTime::class.java,
                LocalDateTimeAdapter()
            )
            .registerTypeAdapter(
                LocalDate::class.java,
                LocalDateAdapter()
            )
            .create()

        return Retrofit
            .Builder()
            .baseUrl("http://192.168.68.87:8080/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit : Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }
}