package com.example.playlistmaker.domain.use_case

interface MediaPlayerInteractor {
        fun preparePlayer(url: String?)
        fun pausePlayer()
        fun playbackControl()
        fun onDestroy()
        fun isStateIsPlaying(): Boolean
        fun getCurrentPosition() : Int
}