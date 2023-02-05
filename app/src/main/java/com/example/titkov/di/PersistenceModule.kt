package com.example.titkov.di

import android.content.Context
import androidx.room.Room
import com.example.titkov.data.persistence.AppDatabase
import com.example.titkov.data.persistence.FavoriteFilmsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PersistenceModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        context: Context
    ): AppDatabase = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "app.db"
    ).build()

    @Provides
    fun provideFavoriteFilmDao(
        appDatabase: AppDatabase
    ): FavoriteFilmsDao = appDatabase.favoriteFilmsDao()
}
