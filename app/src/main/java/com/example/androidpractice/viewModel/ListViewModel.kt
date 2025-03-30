package com.example.androidpractice.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.androidpractice.domain.model.Movie
import com.example.androidpractice.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository: IMoviesRepository,
    private val navigation: NavHostController
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            val movies = repository.getList()
            _viewState.value = _viewState.value.copy(movies = movies)
        }
    }

    fun onItemClicked(id: Int) {
        navigation.navigate("details/$id")
    }

    data class ViewState(
        val movies: List<Movie> = emptyList()
    )
}
