package com.example.androidpractice.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.androidpractice.domain.model.Movie
import com.example.androidpractice.domain.model.Rating
import com.example.androidpractice.ui.theme.Spacing
import com.example.androidpractice.ui.theme.Typography
import com.example.androidpractice.viewModel.DetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailsScreen(navigation: NavHostController, movieId: Int) {
    val viewModel = koinViewModel<DetailsViewModel> { parametersOf(navigation, movieId) }

    MovieScreenContent(
        viewModel.mutableState.movie,
        onBackPressed = { viewModel.back() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieScreenContent(
    movie: Movie?,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = movie?.name.orEmpty(),
                        style = Typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed.invoke() }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        movie ?: run {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "По запросу нет результатов")
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                AsyncImage(
                    model = movie.poster.url,
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .width(130.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = styledText(
                            sectionName = "Год выхода: ",
                            sectionContent = movie.year
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = styledText(
                            sectionName = "Описание: ",
                            sectionContent = movie.description
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }

            Text(
                text = styledText(
                    sectionName = "Жанры: ",
                    sectionContent = movie.genres.joinToString(separator = ", ") {it.name}
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            Text(
                text = styledText(
                    sectionName = "Страны: ",
                    sectionContent = movie.countries.joinToString(separator = ", ") {it.name}
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            Text(text = "В главных ролях:", style = Typography.bodyLarge)
            LazyRow(
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                items(movie.persons) { person ->
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = person.photo,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                        )

                        Spacer(modifier = Modifier.width(Spacing.small))
                        Column {
                            Text(text = person.name, style = Typography.bodyLarge)
                            Text(
                                text = person.profession,
                                style = Typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.medium))

            RatingDisplay(movie.rating)
        }
    }
}

@Composable
fun RatingDisplay(rating: Rating) {
    Row(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Kinopoisk: ${rating.kinopoisk}",
            style = Typography.bodyLarge.copy(color = Color.Gray),
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Text(
            text = "IMDB: ${rating.imdb}",
            style = Typography.bodyLarge.copy(color = Color.Gray),
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Text(
            text = "Rotten Tomatoes: ${rating.rottenTomatoes}",
            style = Typography.bodyLarge.copy(color = Color.Gray),
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}

@Composable
fun styledText(
    sectionName: String,
    sectionContent: String,
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(
            fontWeight = Typography.bodyLarge.fontWeight,
            fontSize = Typography.bodyLarge.fontSize,
            color = Typography.bodyLarge.color
        )) {
            append(sectionName)
        }
        withStyle(style = SpanStyle(
            fontWeight = Typography.bodyMedium.fontWeight,
            fontSize = Typography.bodyMedium.fontSize,
            color = Typography.bodyMedium.color
        )) {
            append(sectionContent)
        }
    }
}
