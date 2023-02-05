package com.example.titkov.data.api

import com.example.titkov.data.api.model.FilmDetailsResponse
import com.example.titkov.data.api.model.TopFilmsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/api/v2.2/films/top?type=TOP_100_POPULAR_FILMS")
    suspend fun getPopularFilms(): TopFilmsResponse

    @GET("/api/v2.2/films/{filmId}")
    suspend fun getFilmDetails(
        @Path("filmId") filmId: Int
    ): FilmDetailsResponse
}