package com.example.androidpractice.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.domain.repository.IMoviesRepository
import com.example.androidpractice.state.FavoritesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: IMoviesRepository
) : ViewModel() {
    private var mutableState = MutableStateFlow(FavoritesState())
    val viewState = mutableState.asStateFlow()

    init {
        updateFavorites()
    }

    fun onUpdateClick() {
        updateFavorites()
    }

    private fun updateFavorites() {
        viewModelScope.launch {
            mutableState.update { it.copy(items = repository.getFavorites()) }
        }
    }
}