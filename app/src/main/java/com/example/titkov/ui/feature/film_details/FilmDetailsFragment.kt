package com.example.titkov.ui.feature.film_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import com.example.titkov.R
import com.example.titkov.domain.model.Film
import com.example.titkov.ui.EventConsumer
import com.example.titkov.ui.appComponent
import com.example.titkov.ui.feature.film_details.FilmDetailsEvent.ClickBack
import com.example.titkov.ui.theme.Background
import com.example.titkov.ui.theme.Blue
import com.example.titkov.ui.theme.TextSecondary
import com.example.titkov.ui.theme.Typography
import com.example.titkov.util.assistedViewModel
import com.example.titkov.util.findInstanceInHierarchy
import com.example.titkov.util.unprovidedCompositionLocalOf
import javax.inject.Inject

val LocalFilmDetailsEventConsumer = unprovidedCompositionLocalOf<EventConsumer<FilmDetailsEvent>>()

class FilmDetailsFragment : Fragment() {
    private val filmId by lazy { requireArguments().getInt(KEY_FILM_ID) }

    @Inject
    lateinit var factory: FilmDetailsViewModel.Factory
    private val viewModel by assistedViewModel {
        factory.create(filmId = filmId, appNavigation = findInstanceInHierarchy())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            CompositionLocalProvider(
                LocalFilmDetailsEventConsumer provides viewModel
            ) {
                val state by viewModel.viewState.collectAsState()
                FilmDetailsContent(state)
            }
        }
    }

    companion object {
        private const val KEY_FILM_ID = "filmId"
        fun newInstance(filmId: Int) = FilmDetailsFragment().apply {
            arguments = bundleOf(KEY_FILM_ID to filmId)
        }
    }
}

@Composable
fun FilmDetailsContent(state: FilmDetailsState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        if (state.isLoading) {
            Loading()
        } else if (state.error != null) {
            Error()
        } else if (state.film != null) {
            FilmDetails(state.film)
        }

        BackButton()
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
    }
}

@Composable
private fun BackButton() {
    val eventConsumer = LocalFilmDetailsEventConsumer.current
    Icon(
        painter = painterResource(id = R.drawable.ic_back),
        contentDescription = null,
        tint = Blue,
        modifier = Modifier
            .padding(20.dp)
            .clickable {
                eventConsumer.consume(ClickBack)
            }
    )
}

@Composable
private fun FilmDetails(film: Film) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        BigBanner(film)

        FilmInfo(film)
    }
}

@Composable
private fun FilmInfo(film: Film) {
    Column(modifier = Modifier.padding(20.dp)) {
        Text(text = film.name, style = Typography.h6)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = film.description, color = TextSecondary)

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.genres), style = LocalTextStyle.current.copy(fontWeight = FontWeight.Medium))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = film.genres.joinToString(", "))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.countries), style = LocalTextStyle.current.copy(fontWeight = FontWeight.Medium))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = film.countries.joinToString(", "))
        }
    }
}

@Composable
private fun BigBanner(film: Film) {
    AsyncImage(
        model = film.bannerUrl,
        contentDescription = null,
        modifier = Modifier.fillMaxWidth().height(500.dp),
        contentScale = ContentScale.Crop
    )
}