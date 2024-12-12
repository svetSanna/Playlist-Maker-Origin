package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Track

interface PlaylistInteractor {
    suspend fun addPlaylist(newplaylist: Playlist) // добавить трек в список избранных
}