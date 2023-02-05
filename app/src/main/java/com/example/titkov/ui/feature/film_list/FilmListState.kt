package com.example.titkov.ui.feature.film_list

import com.example.titkov.domain.model.FilmPreview

data class FilmListState(
    val searchBarQuery: String? = null,
    val isLoading: Boolean = false,
    val currentFilmListType: FilmListType = FilmListType.POPULAR,
    val error: Throwable? = null,
    val films: List<FilmPreview> = emptyList()
)