package com.example.titkov.di

import android.content.Context
import androidx.room.Room
import com.example.titkov.data.FilmRepositoryImpl
import com.example.titkov.data.api.ApiService
import com.example.titkov.data.persistence.AppDatabase
import com.example.titkov.data.persistence.FavoriteFilmsDao
import com.example.titkov.domain.repository.FilmRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, PersistenceModule::class])
interface DataModule {

    @Binds
    fun bindFilmRepository(impl: FilmRepositoryImpl): FilmRepository

}