package com.example.androidpractice.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.androidpractice.data.repository.MoviesRepository
import com.example.androidpractice.data.repository.ProfileRepository
import com.example.androidpractice.domain.repository.IMoviesRepository
import com.example.androidpractice.domain.repository.IProfileRepository
import com.example.androidpractice.mapper.MovieMapper
import com.example.androidpractice.viewModel.DetailsViewModel
import com.example.androidpractice.viewModel.FavoritesViewModel
import com.example.androidpractice.viewModel.ListViewModel
import com.example.androidpractice.viewModel.ProfileViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val rootModule = module {
    single {
        getSharedPrefs(androidApplication())
    }

    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }

    single {
        getDataStore(androidContext())
    }

    single<IMoviesRepository> { MoviesRepository(get(), get(), get()) }
    single<IProfileRepository> { ProfileRepository(get()) }

    factory { MovieMapper() }

    viewModel { ListViewModel(get(), it.get()) }
    viewModel { DetailsViewModel(get(), it.get(), it.get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { ProfileViewModel(get(), it.get()) }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences("default", Context.MODE_PRIVATE)
}

fun getDataStore(androidContext: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create {
        androidContext.preferencesDataStoreFile("default")
    }