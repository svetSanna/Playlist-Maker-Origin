package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.repository.MediaPlayerRepository

class MediaPlayerInteractor(
    private val repositoryImpl: MediaPlayerRepository
) {
    fun preparePlayer(url: String?) {
        return repositoryImpl.preparePlayer(url)
    }

    fun pausePlayer() {
        return repositoryImpl.pausePlayer()
    }

    fun playbackControl() {
        return repositoryImpl.playbackControl()
    }

    fun onDestroy() {
        return repositoryImpl.onDestroy()
    }

    fun isStateIsPlaying(): Boolean{
        return repositoryImpl.isStateIsPlaying()
    }

    fun getCurrentPosition() : Int{
        return repositoryImpl.getCurrentPosition()
    }
}