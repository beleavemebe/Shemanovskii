package com.example.titkov.di

import com.example.titkov.data.api.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
object NetworkModule {
    @Provides
    fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
        val oldRequest = chain.request()

        val newHeaders = oldRequest.headers.newBuilder()
            .add("X-API-KEY: e30ffed0-76ab-4dd6-b41f-4c9da2b2735b")
            .build()

        val newRequest = oldRequest.newBuilder()
            .headers(newHeaders)
            .build()

        chain.proceed(newRequest)
    }

    @Provides
    fun provideOkHttpClient(
        apiKeyInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .build()

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create<ApiService>()
}
