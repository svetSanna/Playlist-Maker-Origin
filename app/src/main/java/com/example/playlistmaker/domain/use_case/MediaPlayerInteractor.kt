package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.repository.MediaPlayerRepository

/*class MediaPlayerInteractor(
    private val repositoryImpl: MediaPlayerRepository
) {
    operator fun invoke(): MediaPlayer {
        return repositoryImpl.getMediaPlayer()
    }
}*/ // p1

class MediaPlayerInteractor(
    // private val activity: Activity,
    private val repositoryImpl: MediaPlayerRepository
) {
    /*fun getCurrentPosition(): Int {
        return repositoryImpl.getCurrentPosition()
    }*/

    fun preparePlayer(url: String?) {
        return repositoryImpl.preparePlayer(url)
    }
    /*fun startPlayer(){
        return repositoryImpl.startPlayer()
    }*/

    fun pausePlayer() {
        return repositoryImpl.pausePlayer()
    }

    fun playbackControl() {
        return repositoryImpl.playbackControl()
    }

    fun onDestroy() {
        return repositoryImpl.onDestroy()
    }
}