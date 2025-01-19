package com.example.homebankfront.hilt

import com.example.homebankfront.data.AuthInterceptor
import com.example.homebankfront.data.services.ApiService
import com.example.homebankfront.data.LocalDateAdapter
import com.example.homebankfront.data.LocalDateTimeAdapter
import com.example.homebankfront.data.repositories.AuthRepository
import com.example.homebankfront.data.services.AuthService
import com.example.homebankfront.data.services.TransactionHeadService
import com.example.homebankfront.security.TokenStorage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
    fun provideRetroFit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.68.87:8080/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(
                LocalDateTime::class.java,
                LocalDateTimeAdapter()
            )
            .registerTypeAdapter(
                LocalDate::class.java,
                LocalDateAdapter()
            )
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionHeadService(retrofit: Retrofit): TransactionHeadService {
        return retrofit.create(TransactionHeadService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenStorage: TokenStorage,
        authService: AuthService
    ): AuthInterceptor {
        return AuthInterceptor(tokenStorage, authService)
    }
}