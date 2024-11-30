package com.example.playlistmaker.presentation.state

import com.example.playlistmaker.domain.entity.Track

// состояния: есть список избранных, список избранных пуст
sealed interface LikeTracksScreenState{
    data class Content(val data: List<Track>) : LikeTracksScreenState
    data class Error(val message : String): LikeTracksScreenState
}

