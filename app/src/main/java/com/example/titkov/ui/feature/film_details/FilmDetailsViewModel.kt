package com.example.titkov.ui.feature.film_details

import com.example.titkov.ui.AppNavigation
import com.example.titkov.ui.BaseViewModel
import com.example.titkov.domain.repository.FilmRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.io.IOException

class FilmDetailsViewModel @AssistedInject constructor(
    @Assisted private val filmId: Int,
    @Assisted private val appNavigation: AppNavigation,
    private val filmRepository: FilmRepository,
) : BaseViewModel<FilmDetailsState, FilmDetailsEvent>() {
    override val initialState = FilmDetailsState(isLoading = true)

    init {
        loadFilm()
    }

    private fun loadFilm() = reduce { prevState ->
        val film = filmRepository.getFilmDetails(filmId)
        prevState.copy(
            isLoading = false,
            film = film
        )
    }

    override fun consume(event: FilmDetailsEvent) {
        when (event) {
            is FilmDetailsEvent.ClickBack -> {
                appNavigation.exitFilmDetails()
            }
        }
    }

    override fun interceptThrowable(throwable: Throwable) {
        throwable.printStackTrace()
        when (throwable) {
            is IOException -> reduce { prevState ->
                prevState.copy(isLoading = false, error = throwable)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            filmId: Int,
            appNavigation: AppNavigation
        ): FilmDetailsViewModel
    }
}