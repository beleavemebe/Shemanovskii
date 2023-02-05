package com.example.titkov.ui.feature.film_list

import com.example.titkov.domain.model.FilmPreview

sealed interface FilmListEvent {
    object ClickReload : FilmListEvent
    object ClickPopular : FilmListEvent
    object ClickFavorites : FilmListEvent
    data class LongPressFilmCard(val filmPreview: FilmPreview) : FilmListEvent
    data class ClickFilmCard(val filmId: Int) : FilmListEvent
    object ClickSearchBar : FilmListEvent
    data class UpdateSearchQuery(val query: String) : FilmListEvent
    object DismissSearch : FilmListEvent
}