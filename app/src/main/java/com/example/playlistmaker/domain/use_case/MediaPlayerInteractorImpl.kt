package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.repository.MediaPlayerRepository

class MediaPlayerInteractorImpl(
    private val repositoryImpl: MediaPlayerRepository) : MediaPlayerInteractor
{
    override fun preparePlayer(url: String?) {
        return repositoryImpl.preparePlayer(url)
    }

    override fun startPlayer() {
        return repositoryImpl.startPlayer()
    }

    override fun pausePlayer() {
        return repositoryImpl.pausePlayer()
    }

    override fun onDestroy() {
        return repositoryImpl.onDestroy()
    }

    override fun isStateIsPlaying(): Boolean{
        return repositoryImpl.isStateIsPlaying()
    }

    override fun getCurrentPosition() : Int{
        return repositoryImpl.getCurrentPosition()
    }
}