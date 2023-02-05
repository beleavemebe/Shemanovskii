package com.example.titkov.data.api.model

data class TopFilmsResponse(
    val films: List<FilmDto>,
    val pagesCount: Int
)

data class FilmDto(
    val filmId: Int,
    val nameRu: String = "",
    val posterUrl: String = "",
    val posterUrlPreview: String = "",
    val year: String = "",
    val genres: List<Genre>,
    val countries: List<Country>,
)