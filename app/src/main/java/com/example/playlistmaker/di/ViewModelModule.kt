package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.view_model.EditPlaylistViewModel
import com.example.playlistmaker.presentation.view_model.LikeTracksViewModel
import com.example.playlistmaker.presentation.view_model.MediaViewModel
import com.example.playlistmaker.presentation.view_model.NewPlaylistViewModel
import com.example.playlistmaker.presentation.view_model.PlaylistViewModel
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
   /* Оставила для примера
   viewModel<MediaViewModel>{(url : String?) ->
        MediaViewModel(mediaPlayerInteractor = get(), url, likeTrackListInteractor = get())
    }
    */
    viewModel<MediaViewModel>{
        MediaViewModel(mediaPlayerInteractor = get(), likeTrackListInteractor = get(), playlistInteractor = get(), track = get())
    }
    viewModel<LikeTracksViewModel>{
        LikeTracksViewModel(likeTrackListInteractor = get(), androidContext())
    }
    viewModel<PlaylistsViewModel>{
        PlaylistsViewModel(playlistInteractor = get(), androidContext())
    }
    viewModel<NewPlaylistViewModel>{
        NewPlaylistViewModel(playlistInteractor = get())
    }
    viewModel<PlaylistViewModel>{
        PlaylistViewModel(playlistInteractor = get(), playlist = get())
    }
    viewModel<EditPlaylistViewModel>{
        EditPlaylistViewModel(playlistInteractor = get(), playlist = get())
    }
}