package com.example.titkov.feature.film_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.AsyncImage
import com.example.titkov.R
import com.example.titkov.feature.film_list.model.Film

class FilmListFragment : Fragment() {
    private val filmListViewModel: FilmListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by filmListViewModel.viewState.collectAsState()
                FilmListContent(state)
            }
        }
    }
}

@Composable
private fun FilmListContent(state: FilmListState) {
    if (state.isLoading) {
        Loading()
    } else if (state.error != null) {
        Error()
    } else {
        Column {
            TopBar()
            FilmList(state.films)
        }
    }
}

@Composable
fun Loading() {

}

@Composable
fun Error() {

}

@Composable
fun TopBar() {

}

@Composable
fun FilmList(films: List<Film>) {
    LazyColumn {
        items(films) { film ->
            FilmCard(film)
        }
    }
}

@Composable
fun FilmCard(film: Film) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp
    ) {
        Row {
            FilmBanner(film)
            FilmInfo(film)
            AnimatedVisibility(visible = film.isInFavorites) {
                FavoriteFilmIcon()
            }
        }
    }
}

@Composable
fun FilmBanner(film: Film) {
    AsyncImage(
        model = film.posterUrlPreview,
        contentDescription = null,
    )
}

@Composable
fun FilmInfo(film: Film) {
    Column {
        Text(text = film.nameRu)
        Text(text = stringResource(R.string.genre_and_year_placeholder, film.genres[0], film.year))
    }
}

@Composable
fun FavoriteFilmIcon() {

}
