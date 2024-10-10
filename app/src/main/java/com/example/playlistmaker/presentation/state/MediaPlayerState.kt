package com.example.playlistmaker.presentation.state

sealed interface MediaPlayerState {
    object Playing : MediaPlayerState
    object Paused : MediaPlayerState
    object Prepared : MediaPlayerState
}
