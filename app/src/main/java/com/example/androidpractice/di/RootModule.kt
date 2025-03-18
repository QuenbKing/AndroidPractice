package com.example.androidpractice.di

import com.example.androidpractice.data.repository.MoviesRepository
import com.example.androidpractice.domain.repository.IMoviesRepository
import com.example.androidpractice.viewModel.DetailsViewModel
import com.example.androidpractice.viewModel.ListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val rootModule = module {
    single<IMoviesRepository> { MoviesRepository() }

    viewModel { ListViewModel(get(), it.get()) }
    viewModel { DetailsViewModel(get(), it.get(), it.get()) }
}