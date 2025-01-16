package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.PLAYLISTMAKER_PREFERENCES
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.history.SearchHistory
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TrackApi
import com.example.playlistmaker.data.repository.LikeTrackListRepositoryImpl
import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.NetworkClient
import com.example.playlistmaker.data.repository.PlaylistRepositoryImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.SharedPreferencesRepositoryImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.domain.repository.LikeTrackListRepository
import com.example.playlistmaker.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.repository.PlaylistRepository
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.SharedPreferencesRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {
    single {
        androidContext()
            .getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)

    }

    factory { Gson() }

    single<TrackApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(trackApiService = get(), androidContext())
    }

    single<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(mediaPlayer = get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(searchHistory = get())
    }

    single<SearchHistory> {
        SearchHistory(sharedPrefs = get(), json = get())
    }

    single<SharedPreferencesRepository> {
        SharedPreferencesRepositoryImpl(sharedPrefs = get())
    }

    single<TrackRepository> {
        TrackRepositoryImpl(networkClient = get())
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "AppDatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<LikeTrackListRepository> {
        LikeTrackListRepositoryImpl(appDatabase = get(), trackDbConverter = get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            appDatabase = get(),
            playlistDbConverter = get(),
            trackInPlaylistDbConverter = get()
        )
    }
}
