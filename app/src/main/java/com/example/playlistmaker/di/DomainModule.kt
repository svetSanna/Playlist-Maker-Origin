package com.example.playlistmaker.di

import com.example.playlistmaker.domain.use_case.GetTrackListUseCase
import com.example.playlistmaker.domain.use_case.MediaPlayerInteractor
import com.example.playlistmaker.domain.use_case.MediaPlayerInteractorImpl
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
}
