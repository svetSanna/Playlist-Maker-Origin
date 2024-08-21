package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.PLAYLISTMAKER_PREFERENCES
import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.NetworkClient
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.domain.use_case.GetMediaPlayerUseCase
import com.example.playlistmaker.domain.use_case.GetTrackListUseCase

object Creator {
    // поле, которое отвечает за контекст
    /*private lateinit var application: Application

    fun initApplication(application: Application){ // передаем сюда контекст приложения
        this.application = application
    }
    */
    private lateinit var application: AppCompatActivity

    fun initApplication(application: AppCompatActivity) { // передаем сюда контекст приложения
        this.application = application
    }

    fun provideSharedPreferences(): SharedPreferences {
        // на основе контекста получаем SharedPreferences
        return application.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun provideGetMediaPlayerUseCase(): GetMediaPlayerUseCase {
        return GetMediaPlayerUseCase(provideMediaPlayer())
    }

    private fun provideMediaPlayer(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    fun provideGetTrackListUseCase(): GetTrackListUseCase{
        return GetTrackListUseCase(provideTrackRepository())
    }

    private fun provideTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(provideNetworkClient())
    }

    private fun provideNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }

   // fun provideGetTrackInteractor(): TrackInteractor {
   //     return GetTrackListUseCase(getTrackRepository())
   // }
}