package com.example.playlistmaker.domain.use_case

interface MediaPlayerInteractor {
        fun preparePlayer(url: String?)
        fun startPlayer()
        fun pausePlayer()
        fun onDestroy()
        fun isStateIsPlaying(): Boolean
        fun getCurrentPosition() : Int
}