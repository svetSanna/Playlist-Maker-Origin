package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl : MediaPlayerRepository {
    override fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }
}