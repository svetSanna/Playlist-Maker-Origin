package com.example.playlistmaker.presentation.state

import com.example.playlistmaker.domain.entity.Playlist

interface PlaylistsScreenState {
    data class Content(val data: List<Playlist>) : PlaylistsScreenState
    data class Error(val message : String): PlaylistsScreenState
}