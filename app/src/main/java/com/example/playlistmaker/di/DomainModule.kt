package com.example.playlistmaker.di

import com.example.playlistmaker.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.data.db.converters.LikeTrackDbConverter
import com.example.playlistmaker.data.db.converters.TrackInPlaylistDbConverter
import com.example.playlistmaker.domain.use_case.LikeTrackListInteractorImpl
import com.example.playlistmaker.domain.use_case.GetTrackListUseCase
import com.example.playlistmaker.domain.use_case.LikeTrackListInteractor
import com.example.playlistmaker.domain.use_case.MediaPlayerInteractor
import com.example.playlistmaker.domain.use_case.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.use_case.PlaylistInteractor
import com.example.playlistmaker.domain.use_case.PlaylistInteractorImpl
import com.example.playlistmaker.domain.use_case.SearchHistoryInteractor
import com.example.playlistmaker.domain.use_case.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.use_case.SharedPreferencesInteractor
import com.example.playlistmaker.domain.use_case.SharedPreferencesInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    factory<SharedPreferencesInteractor>{
        SharedPreferencesInteractorImpl(repository = get())
    }
    factory<SearchHistoryInteractor>{
        SearchHistoryInteractorImpl(repositoryImpl = get())
    }
    factory<GetTrackListUseCase>{
        GetTrackListUseCase(repository = get())
    }
    factory<MediaPlayerInteractor>{
        MediaPlayerInteractorImpl(repositoryImpl = get())
    }
    factory {
        LikeTrackDbConverter()
    }
    factory<LikeTrackListInteractor>{
        LikeTrackListInteractorImpl(repository = get())
    }
    factory {
        PlaylistDbConverter()
    }
    factory<PlaylistInteractor>{
        PlaylistInteractorImpl(repository = get())
    }
    factory {
        TrackInPlaylistDbConverter()
    }
}
