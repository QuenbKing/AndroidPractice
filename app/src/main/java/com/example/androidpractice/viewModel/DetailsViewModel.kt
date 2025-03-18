package com.example.androidpractice.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.androidpractice.domain.model.Movie
import com.example.androidpractice.domain.repository.IMoviesRepository

class DetailsViewModel(
    private val repository: IMoviesRepository,
    private val navigation: NavHostController,
    private val id: Int
) : ViewModel() {

    val mutableState = MutableDetailsState()

    init {
        mutableState.movie = repository.getById(id)
    }

    fun back() {
        navigation.popBackStack()
    }

    class MutableDetailsState {
        var movie: Movie? by mutableStateOf(null)
    }
}