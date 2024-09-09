package com.example.playlistmaker.creator

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.PLAYLISTMAKER_PREFERENCES
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.NetworkClient
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.SharedPreferencesRepositoryImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.SharedPreferencesRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.domain.use_case.GetTrackListUseCase
import com.example.playlistmaker.domain.use_case.MediaPlayerInteractor
import com.example.playlistmaker.domain.use_case.SearchHistoryInteractor
import com.example.playlistmaker.domain.use_case.SharedPreferencesInteractor

object Creator {
    // поле, которое отвечает за контекст
    /*private lateinit var application: Application

    fun initApplication(application: Application){ // передаем сюда контекст приложения
        this.application = application
    }
    */
    private lateinit var application: Application    //private lateinit var application: AppCompatActivity //p2

    /*    fun initApplication(application: AppCompatActivity) { // передаем сюда контекст приложения
            this.application = application
        }
    */ //p2
    fun initApplication(application: Application) { // передаем сюда контекст приложения  //p2
        this.application = application
    }

    fun provideSharedPreferences(): SharedPreferences {
        // на основе контекста получаем SharedPreferences
        return application.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun provideMediaPlayerInteractor(activity: Activity): MediaPlayerInteractor {
        //  return MediaPlayerInteractor(activity, provideMediaPlayer(activity))
        return MediaPlayerInteractor(provideMediaPlayer(activity))
    }

    private fun provideMediaPlayer(activity: Activity): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(activity)
    }

    fun provideSharedPreferencesInteractor(): SharedPreferencesInteractor {
        return SharedPreferencesInteractor(provideSharedPreferencesRepository())
    }

    private fun provideSharedPreferencesRepository(): SharedPreferencesRepository {
        return SharedPreferencesRepositoryImpl()
    }

    fun provideGetTrackListUseCase(): GetTrackListUseCase {
        return GetTrackListUseCase(provideTrackRepository())
    }

    private fun provideTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(provideNetworkClient())
    }

    private fun provideNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }

    /*fun provideGetSearchHistoryInteractor(sharedPrefs: SharedPreferences): SearchHistoryInteractor {
        return SearchHistoryInteractor(provideSearchHistoryInteractor(sharedPrefs))
    }*/ //p3
    fun provideGetSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractor(provideSearchHistoryInteractor())
    }

    /*private fun provideSearchHistoryInteractor(sharedPrefs: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPrefs)
    }*/ //p3
    private fun provideSearchHistoryInteractor(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl()
    }
}