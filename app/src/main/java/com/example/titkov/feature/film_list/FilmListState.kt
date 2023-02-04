package com.example.titkov.feature.film_list

import com.example.titkov.feature.film_list.model.Film

data class FilmListState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val films: List<Film> = emptyList()
)