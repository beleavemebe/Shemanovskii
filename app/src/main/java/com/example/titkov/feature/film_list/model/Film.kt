package com.example.titkov.feature.film_list.model

data class Film(
    val filmId: Int,
    val nameRu: String = "",
    val posterUrl: String = "",
    val posterUrlPreview: String = "",
    val year: String = "",
    val genres: List<Genre>,
    val countries: List<Country>,
    val isInFavorites: Boolean = false,
//    val rating: String,
//    val ratingChange: Any,
//    val ratingVoteCount: Int,
//    val filmLength: String,
//    val nameEn: String,
)