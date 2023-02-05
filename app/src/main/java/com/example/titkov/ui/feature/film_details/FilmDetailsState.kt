package com.example.titkov.ui.feature.film_details

import com.example.titkov.domain.model.Film

data class FilmDetailsState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val film: Film? = null,
)
