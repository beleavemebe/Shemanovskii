package com.example.titkov.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_film")
data class FavoriteFilmEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val filmId: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "banner_url")
    val bannerUrl: String,

    @ColumnInfo(name = "year")
    val year: String,

    @ColumnInfo(name = "genre")
    val genre: String
)