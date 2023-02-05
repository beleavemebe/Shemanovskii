package com.example.titkov.ui.feature.film_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.titkov.ui.EventConsumer
import com.example.titkov.R
import com.example.titkov.domain.model.FilmPreview
import com.example.titkov.ui.appComponent
import com.example.titkov.ui.feature.film_list.FilmListEvent.*
import com.example.titkov.ui.theme.*
import com.example.titkov.util.assistedViewModel
import com.example.titkov.util.findInstanceInHierarchy
import com.example.titkov.util.unprovidedCompositionLocalOf
import javax.inject.Inject

val LocalFilmListEventConsumer = unprovidedCompositionLocalOf<EventConsumer<FilmListEvent>>()

class FilmListFragment : Fragment() {
    @Inject
    lateinit var factory: FilmListViewModel.Factory
    private val viewModel: FilmListViewModel by assistedViewModel {
        factory.create(appNavigation = findInstanceInHierarchy())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                CompositionLocalProvider(
                    LocalFilmListEventConsumer provides viewModel
                ) {
                    val state by viewModel.viewState.collectAsState()
                    FilmListContent(state)
                }
            }
        }
    }
}

@Composable
private fun FilmListContent(state: FilmListState) {
    Column {
        TopBar(state.currentFilmListType, state.searchBarQuery)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (state.isLoading) {
                Loading()
            } else if (state.error != null) {
                Error()
            } else if (state.films.isEmpty()) {
                Emptiness(state.currentFilmListType)
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                FilmList(state.films)
            }
        }

        BottomNavigationButtons(state.currentFilmListType)
    }
}

@Composable
private fun BottomNavigationButtons(filmListType: FilmListType) {
    val eventConsumer = LocalFilmListEventConsumer.current
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        when (filmListType) {
            FilmListType.POPULAR -> {
                AccentButton(
                    text = stringResource(id = R.string.popular),
                    onClick = {
                        eventConsumer.consume(ClickPopular)
                    }
                )
                Button(
                    text = stringResource(id = R.string.favorites),
                    onClick = {
                        eventConsumer.consume(ClickFavorites)
                    }
                )
            }
            FilmListType.FAVORITES -> {
                Button(
                    text = stringResource(id = R.string.popular),
                    onClick = {
                        eventConsumer.consume(ClickPopular)
                    }
                )
                AccentButton(
                    text = stringResource(id = R.string.favorites),
                    onClick = {
                        eventConsumer.consume(ClickFavorites)
                    }
                )
            }
        }
    }
}

@Composable
private fun Loading() {
    CircularProgressIndicator(
        color = Blue
    )
}

@Composable
private fun Error() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(40.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error),
            contentDescription = null,
            tint = Blue,
            modifier = Modifier.size(100.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.network_error),
            style = Typography.body1.copy(fontWeight = FontWeight.Medium),
            color = Blue,
        )

        Spacer(modifier = Modifier.height(36.dp))

        val eventConsumer = LocalFilmListEventConsumer.current
        AccentButton(
            text = stringResource(R.string.retry),
            onClick = {
                eventConsumer.consume(ClickReload)
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun AccentButton(text: String, onClick: () -> Unit) {
    Surface(
        selected = false,
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        color = Blue
    ) {
        Text(
            text = text,
            color = OnBlue,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        )
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun Button(
    text: String,
    onClick: () -> Unit,
) {
    Surface(
        selected = false,
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        color = Cyan
    ) {
        Text(
            text = text,
            color = Blue,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        )
    }
}

@Composable
private fun Emptiness(filmListType: FilmListType) {
    Box(Modifier.padding(40.dp)) {
        Text(
            text = when (filmListType) {
                FilmListType.POPULAR -> stringResource(R.string.not_found)
                FilmListType.FAVORITES -> stringResource(R.string.hint_no_favorites)
            },
            color = Blue
        )
    }
}

@Composable
private fun TopBar(
    filmListType: FilmListType,
    searchBarQuery: String? = null
) {
    if (searchBarQuery == null) {
        IdleTopBar(filmListType)
    } else {
        SearchBar(searchBarQuery)
    }
}

@Composable
private fun IdleTopBar(filmListType: FilmListType) {
    val eventConsumer = LocalFilmListEventConsumer.current
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        Text(
            text = when (filmListType) {
                FilmListType.POPULAR -> stringResource(R.string.popular)
                FilmListType.FAVORITES -> stringResource(R.string.favorites)
            },
            style = Typography.h6,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = null,
            tint = Blue,
            modifier = Modifier.clickable {
                eventConsumer.consume(ClickSearchBar)
            }
        )
    }
}

@Composable
fun SearchBar(searchBarQuery: String) {
    val eventConsumer = LocalFilmListEventConsumer.current
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = null,
            tint = Blue,
            modifier = Modifier.clickable {
                eventConsumer.consume(DismissSearch)
            }
        )

        Spacer(modifier = Modifier.width(20.dp))

        val focusRequester = remember { FocusRequester() }

        BasicTextField(
            value = searchBarQuery,
            onValueChange = { query ->
                eventConsumer.consume(UpdateSearchQuery(query))
            },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
        )

        LaunchedEffect(null) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
private fun FilmList(films: List<FilmPreview>) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxHeight()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(films) { FilmPreview ->
                FilmCard(FilmPreview)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun FilmCard(filmPreview: FilmPreview) {
    val eventConsumer = LocalFilmListEventConsumer.current
    Surface(
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onLongPress = {
                    eventConsumer.consume(LongPressFilmCard(filmPreview))
                },
                onTap = {
                    eventConsumer.consume(ClickFilmCard(filmPreview.id))
                }
            )
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            FilmBanner(filmPreview)

            Spacer(modifier = Modifier.width(16.dp))

            FilmInfo(filmPreview)

            AnimatedVisibility(
                visible = filmPreview.isInFavorites
            ) {
                FavoriteFilmIcon()
            }
        }
    }
}

@Composable
private fun FilmBanner(filmPreview: FilmPreview) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(filmPreview.bannerUrl)
            .transformations(RoundedCornersTransformation(20f))
            .build(),
        contentDescription = null,
        modifier = Modifier
            .width(40.dp)
            .height(60.dp)
    )
}

@Composable
private fun RowScope.FilmInfo(filmPreview: FilmPreview) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
    ) {
        Text(
            text = filmPreview.name,
            style = Typography.body1.copy(fontWeight = FontWeight.Medium)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(
                id = R.string.genre_and_year_placeholder,
                filmPreview.genre,
                filmPreview.year
            ),
            style = Typography.body2.copy(color = TextSecondary)
        )
    }
}

@Composable
private fun FavoriteFilmIcon() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_favorite),
            tint = Blue,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}
