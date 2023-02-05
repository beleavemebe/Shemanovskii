package com.example.titkov.domain.repository

import com.example.titkov.domain.model.Film
import com.example.titkov.domain.model.FilmPreview
import kotlinx.coroutines.flow.Flow

interface FilmRepository {
    val popularFilmsFlow: Flow<List<FilmPreview>>
    val favoriteFilmsFlow: Flow<List<FilmPreview>>
    fun setSearchQuery(query: String?)
    suspend fun loadPopularFilms()
    suspend fun getFilmDetails(filmId: Int): Film
    suspend fun putFilmInFavorites(filmPreview: FilmPreview)
}
