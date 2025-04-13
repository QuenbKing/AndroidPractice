package com.example.androidpractice.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.androidpractice.domain.model.MovieShort
import com.example.androidpractice.ui.theme.Spacing
import com.example.androidpractice.ui.theme.Typography
import com.example.androidpractice.viewModel.ListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ListScreen(navigation: NavHostController) {
    val viewModel = koinViewModel<ListViewModel> { parametersOf(navigation) }
    val state = viewModel.viewState
    Scaffold(
        topBar = {
            OutlinedTextField(
                value = state.query,
                onValueChange = {
                    viewModel.onQueryChanged(it)
                },
                label = { Text("Введите название фильма") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.medium)
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            FullscreenLoading()
            return@Scaffold
        }

        state.error?.let {
            FullscreenMessage(msg = it)
            return@Scaffold
        }

        if (state.isEmpty) {
            FullscreenMessage("По запросу нет результатов")
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MovieList(
                state.items,
                onMovieClick = { movieId -> viewModel.onItemClicked(movieId) }
            )
        }
    }
}

@Composable
fun MovieList(list: List<MovieShort>, onMovieClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.medium)
    ) {
        items(list) { movie ->
            MovieCard(movie, onMovieClick)
        }
    }
}

@Composable
fun MovieCard(movie: MovieShort, onMovieClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(4.dp),
        onClick = { onMovieClick(movie.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = movie.poster.previewUrl,
                contentDescription = "Постер фильма",
                modifier = Modifier
                    .weight(1.5f)
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = movie.name,
                style = Typography.titleLarge,
                modifier = Modifier.weight(2f)
            )
        }
    }
}

@Composable
fun FullscreenMessage(msg: String) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(Spacing.medium), contentAlignment = Alignment.Center
    ) {
        Text(text = msg)
    }
}

@Composable
fun FullscreenLoading() {
    Box(
        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}