package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.PLAYLISTMAKER_PREFERENCES
import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.use_case.GetMediaPlayerUseCase

object Creator {
    // поле, которое отвечает за контекст
    /*private lateinit var application: Application

    fun initApplication(application: Application){ // передаем сюда контекст приложения
        this.application = application
    }
    */
    private lateinit var application: AppCompatActivity

    fun initApplication(application: AppCompatActivity){ // передаем сюда контекст приложения
        this.application = application
    }

    fun provideSharedPreferences(): SharedPreferences {
        // на основе контекста получаем SharedPreferences
        return application.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun provideGetMediaPlayerUseCase(): GetMediaPlayerUseCase{
        return GetMediaPlayerUseCase(provideMediaPlayer())
    }

    private fun provideMediaPlayer(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }
}