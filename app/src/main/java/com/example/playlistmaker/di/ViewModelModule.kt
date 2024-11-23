package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.presentation.view_model.MediaViewModel
import com.example.playlistmaker.presentation.view_model.PlaylistsViewModel
import com.example.playlistmaker.presentation.view_model.SearchViewModel
import com.example.playlistmaker.presentation.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<SettingsViewModel>{
        SettingsViewModel(sharedPreferencesInteractor = get())
    }
    viewModel<SearchViewModel>{
        SearchViewModel(getTrackListUseCase = get(), historyInteractor = get(), androidContext())
    }
    viewModel<MediaViewModel>{(url : String?) ->
        MediaViewModel(mediaPlayerInteractor = get(), url, likeTrackListUseCase = get())
    }
    viewModel<FavoriteTracksViewModel>{
        FavoriteTracksViewModel()
    }
    viewModel<PlaylistsViewModel>{
        PlaylistsViewModel()
    }
}