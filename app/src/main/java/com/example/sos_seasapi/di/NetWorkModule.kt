package com.example.sos_seasapi.di

import com.example.sos_seasapi.data.repository.GestoRepositoryImpl
import com.example.sos_seasapi.data.service.GestoApiService
import com.example.sos_seasapi.domain.repository.GestoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://j5s4m6z5-5000.brs.devtunnels.ms/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // 👈 añadimos el cliente aquí
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideGestoApiService(retrofit: Retrofit): GestoApiService =
        retrofit.create(GestoApiService::class.java)

    @Provides
    @Singleton
    fun provideGestoRepository(
        api: GestoApiService
    ): GestoRepository = GestoRepositoryImpl(api)
}
