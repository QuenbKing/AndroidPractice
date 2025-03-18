package com.example.androidpractice.viewModel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.androidpractice.domain.model.Movie
import com.example.androidpractice.domain.repository.IMoviesRepository

class ListViewModel(
    private val repository: IMoviesRepository,
    private val navigation: NavHostController
) : ViewModel() {

    init {
        loadMovies()
    }

    fun loadMovies(): List<Movie> {
        return repository.getList()
    }

    fun onItemClicked(id: Int) {
        navigation.navigate("details/$id")
    }
}