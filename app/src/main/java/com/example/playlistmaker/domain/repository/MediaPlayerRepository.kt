package com.example.playlistmaker.domain.repository

import android.media.MediaPlayer

interface MediaPlayerRepository {
    fun getMediaPlayer(): MediaPlayer
}