package com.example.titkov.data

import com.example.titkov.data.api.ApiService
import com.example.titkov.data.api.model.FilmDetailsResponse
import com.example.titkov.data.api.model.FilmDto
import com.example.titkov.data.cache.CachedResource
import com.example.titkov.data.cache.cacheResult
import com.example.titkov.data.persistence.FavoriteFilmEntity
import com.example.titkov.data.persistence.FavoriteFilmsDao
import com.example.titkov.domain.model.Film
import com.example.titkov.domain.model.FilmPreview
import com.example.titkov.domain.repository.FilmRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_POPULAR_FILMS_CACHE = "POPULAR_FILMS"
private const val KEY_FILM_DETAILS_CACHE_PREFIX = "FILM_DETAILS_"

@Singleton
class FilmRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val favoriteFilmsDao: FavoriteFilmsDao,
) : FilmRepository, CachedResource by CachedResource.Delegate() {
    private val favFilmsFlow = favoriteFilmsDao.getDataFlow()
    private val _popularFilmsFlow = MutableSharedFlow<List<FilmPreview>>()
    private val searchQuery = MutableStateFlow<String?>(null)

//    override val popularFilmsFlow =
//        _popularFilmsFlow.combine(favFilmsFlow) { topFilms, favoriteFilms ->
//            val favFilmIds = favoriteFilms.map { it.filmId }
//
//            topFilms.map { film ->
//                if (film.id !in favFilmIds) {
//                    film
//                } else {
//                    film.copy(isInFavorites = true)
//                }
//            }
//        }.combine(searchQuery) { films, q ->
//            films.matchedToQuery(q)
//        }
//
    override val popularFilmsFlow = combine(
        _popularFilmsFlow, favFilmsFlow, searchQuery
    ) { topFilms, favoriteFilms, query ->
        val favFilmIds = favoriteFilms.map { it.filmId }

        topFilms.map { film ->
            if (film.id !in favFilmIds) {
                film
            } else {
                film.copy(isInFavorites = true)
            }
        }.matchedToQuery(query)
    }

    override fun setSearchQuery(query: String?) {
        searchQuery.value = query
    }

    override suspend fun loadPopularFilms() {
        _popularFilmsFlow.emit(
            getPopularFilms(forceUpdate = true)
        )
    }

    private suspend fun getPopularFilms(forceUpdate: Boolean): List<FilmPreview> =
        cacheResult(KEY_POPULAR_FILMS_CACHE, forceUpdate) {
            apiService.getPopularFilms().films
                .map { filmDto ->
                    filmDto.toFilmPreview()
                }
        }

    private suspend fun FilmDto.toFilmPreview(): FilmPreview {
        return FilmPreview(
            id = filmId,
            name = nameRu,
            bannerUrl = posterUrlPreview,
            year = year,
            genre = genres[0].genre.replaceFirstChar { it.titlecase() },
            isInFavorites = isFilmInFavorites(filmId)
        )
    }

    override suspend fun getFilmDetails(filmId: Int): Film =
        cacheResult("$KEY_FILM_DETAILS_CACHE_PREFIX$filmId") {
            apiService.getFilmDetails(filmId).toFilmDetails()
        }

    private suspend fun FilmDetailsResponse.toFilmDetails(): Film {
        return Film(
            id = kinopoiskId,
            name = nameRu,
            description = description,
            bannerUrl = posterUrl,
            year = year.toString(),
            genres = genres.map { it.genre },
            countries = countries.map { it.country },
            isInFavorites = isFilmInFavorites(kinopoiskId)
        )
    }

    override val favoriteFilmsFlow: Flow<List<FilmPreview>>
        get() = favoriteFilmsDao.getDataFlow()
            .map { entities ->
                entities.map {
                    it.toFilmPreview()
                }
            }.map {
                it.matchedToQuery(searchQuery.value)
            }

    private fun FavoriteFilmEntity.toFilmPreview(): FilmPreview {
        return FilmPreview(
            id = filmId,
            name = name,
            bannerUrl = bannerUrl,
            year = year,
            genre = genre,
            isInFavorites = true,
        )
    }

    private suspend fun isFilmInFavorites(filmId: Int): Boolean {
        return favoriteFilmsDao.findByFilmId(filmId) != null
    }

    override suspend fun putFilmInFavorites(filmPreview: FilmPreview) {
        favoriteFilmsDao.insert(
            FavoriteFilmEntity(
                filmId = filmPreview.id,
                name = filmPreview.name,
                bannerUrl = filmPreview.bannerUrl,
                year = filmPreview.year,
                genre = filmPreview.genre
            )
        )
    }

    private fun List<FilmPreview>.matchedToQuery(
        query: String? = searchQuery.value
    ): List<FilmPreview> {
        return if (query == null) {
            this
        } else mapNotNull { preview ->
            preview.takeIf { it.matchesQuery(query) }
        }
    }
}
