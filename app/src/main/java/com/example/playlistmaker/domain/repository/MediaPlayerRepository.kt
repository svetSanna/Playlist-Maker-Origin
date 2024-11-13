package com.example.playlistmaker.domain.repository

interface MediaPlayerRepository {
    fun getCurrentPosition(): Int
    fun preparePlayer(url: String?)
    fun startPlayer()
    fun pausePlayer()
    fun onDestroy()
    fun isStateIsPlaying(): Boolean
}