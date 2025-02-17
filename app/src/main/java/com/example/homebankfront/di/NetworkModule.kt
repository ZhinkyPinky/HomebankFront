package com.example.homebankfront.di

import com.example.homebankfront.BuildConfig
import com.example.homebankfront.data.LocalDateAdapter
import com.example.homebankfront.data.LocalDateTimeAdapter
import com.example.homebankfront.data.remote.services.AuthService
import com.example.homebankfront.data.remote.services.CustomerService
import com.example.homebankfront.data.remote.services.TransactionHeadService
import com.example.homebankfront.data.remote.services.TransactionRowService
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.network.HTTPErrorHandlerContext
import com.example.homebankfront.network.RequestHandler
import com.example.homebankfront.network.RequestInterceptor
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
            .baseUrl(BuildConfig.API_BASE_URL)
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
    fun provideOkHttpClient(requestInterceptor: RequestInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(requestInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): CustomerService {
        return retrofit.create(CustomerService::class.java)
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
    fun provideTransactionRowService(retrofit: Retrofit): TransactionRowService {
        return retrofit.create(TransactionRowService::class.java)
    }

    @Provides
    @Singleton
    fun provideRequestInterceptor(requestHandler: RequestHandler): RequestInterceptor {
        return RequestInterceptor(requestHandler)
    }

    @Provides
    @Singleton
    fun provideRequestHandler(
        tokenStorage: TokenStorage,
        eventEmitter: EventEmitter<NetworkError>,
        errorHandlerContext: HTTPErrorHandlerContext
    ): RequestHandler {
        return RequestHandler(tokenStorage, eventEmitter, errorHandlerContext)
    }
}