package com.example.playlistmaker.presentation.state

sealed interface MediaPlayerState {
   // object Loading : MediaPlayerState
    object Playing : MediaPlayerState
    object Paused : MediaPlayerState
    object Prepared : MediaPlayerState

    //data class Play() : MediaPlayerState
    //data class Stop(): MediaPlayerState
}
