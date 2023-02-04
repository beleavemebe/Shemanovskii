package com.example.titkov.feature.film_list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilmListViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(FilmListState())
    val viewState: StateFlow<FilmListState> = _viewState.asStateFlow()
}
