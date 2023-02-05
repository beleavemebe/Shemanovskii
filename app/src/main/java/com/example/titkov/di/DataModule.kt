package com.example.titkov.di

import com.example.titkov.data.FilmRepositoryImpl
import com.example.titkov.domain.repository.FilmRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, PersistenceModule::class])
interface DataModule {

    @Binds
    fun bindFilmRepository(
        impl: FilmRepositoryImpl
    ): FilmRepository

}