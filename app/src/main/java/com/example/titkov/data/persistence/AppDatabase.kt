package com.example.titkov.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteFilmEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteFilmsDao(): FavoriteFilmsDao
}