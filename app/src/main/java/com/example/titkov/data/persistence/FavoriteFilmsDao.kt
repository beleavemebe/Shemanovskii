package com.example.titkov.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFilmsDao {
    @Query("SELECT * FROM favorite_film")
    fun getDataFlow(): Flow<List<FavoriteFilmEntity>>

    @Query("SELECT * FROM favorite_film")
    suspend fun findAll(): List<FavoriteFilmEntity>

    @Query("SELECT * FROM favorite_film WHERE id = (:filmId)")
    suspend fun findByFilmId(filmId: Int): FavoriteFilmEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteFilmEntity)
}