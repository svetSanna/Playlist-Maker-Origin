package com.example.playlistmaker.domain.use_case

import android.media.MediaPlayer
import com.example.playlistmaker.domain.repository.MediaPlayerRepository

class GetMediaPlayerUseCase(
    private val repositoryImpl: MediaPlayerRepository
) {
    operator fun invoke(): MediaPlayer{
        return repositoryImpl.getMediaPlayer()
    }
}