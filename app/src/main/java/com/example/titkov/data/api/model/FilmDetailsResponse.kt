package com.example.titkov.data.api.model

data class FilmDetailsResponse(
    val kinopoiskId: Int,
    val countries: List<Country>,
    val coverUrl: String,
    val description: String,
    val genres: List<Genre>,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val year: Int
)