package com.example.titkov.ui.feature.film_list

import com.example.titkov.domain.model.FilmPreview
import com.example.titkov.domain.repository.FilmRepository
import com.example.titkov.ui.AppNavigation
import com.example.titkov.ui.BaseViewModel
import com.example.titkov.ui.feature.film_list.FilmListType.FAVORITES
import com.example.titkov.ui.feature.film_list.FilmListType.POPULAR
import com.example.titkov.ui.state
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.flatMapLatest
import java.io.IOException

class FilmListViewModel @AssistedInject constructor(
    @Assisted private val appNavigation: AppNavigation,
    private val filmRepository: FilmRepository,
) : BaseViewModel<FilmListState, FilmListEvent>() {

    override val initialState = FilmListState(isLoading = true)

    override fun consume(event: FilmListEvent) {
        when (event) {
            is FilmListEvent.ClickReload -> reloadFilms()
            is FilmListEvent.ClickPopular -> switchFilmList(POPULAR)
            is FilmListEvent.ClickFavorites -> switchFilmList(FAVORITES)
            is FilmListEvent.LongPressFilmCard -> {
                if (state.currentFilmListType != FAVORITES) {
                    putFilmInFavorites(event.filmPreview)
                }
            }
            is FilmListEvent.ClickFilmCard -> appNavigation.goToFilmDetails(event.filmId)
            is FilmListEvent.ClickSearchBar -> updateSearchQuery("")
            is FilmListEvent.UpdateSearchQuery -> updateSearchQuery(event.query)
            is FilmListEvent.DismissSearch -> updateSearchQuery(null)
        }
    }

    init {
        reloadFilms()
        subscribeToContentFlow()
    }

    private fun reloadFilms() = execute {
        showLoader()
        filmRepository.loadPopularFilms()
    }

    private fun showLoader() = reduce { prevState ->
        prevState.copy(isLoading = true)
    }

    private fun subscribeToContentFlow() = execute {
        viewState.flatMapLatest { state ->
            when (state.currentFilmListType) {
                POPULAR -> filmRepository.popularFilmsFlow
                FAVORITES -> filmRepository.favoriteFilmsFlow
            }
        }.collect(::updateFilms)
    }

    private fun updateFilms(films: List<FilmPreview>) = reduce { prevState ->
        prevState.copy(isLoading = false, films = films, error = null)
    }

    private fun updateSearchQuery(query: String?) = reduce { prevState ->
        filmRepository.setSearchQuery(query)
        prevState.copy(searchBarQuery = query)
    }

    private fun switchFilmList(type: FilmListType) = reduce { prevState ->
        if (state.currentFilmListType == type) {
            prevState
        } else {
            reloadFilms()
            prevState.copy(currentFilmListType = type, error = null)
        }
    }

    private fun putFilmInFavorites(filmPreview: FilmPreview) = execute {
        filmRepository.putFilmInFavorites(filmPreview)
    }

    override fun interceptThrowable(throwable: Throwable) {
        throwable.printStackTrace()
        when (throwable) {
            is IOException -> if (state.currentFilmListType == POPULAR) {
                showError(throwable)
            }
        }
    }

    private fun showError(throwable: Throwable) = reduce { prevState ->
        prevState.copy(
            isLoading = false,
            error = throwable
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(appNavigation: AppNavigation): FilmListViewModel
    }
}
