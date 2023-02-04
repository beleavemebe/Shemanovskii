package com.example.titkov.feature.film_list.model

data class FilmsResponse(
    val films: List<Film>,
    val pagesCount: Int
)