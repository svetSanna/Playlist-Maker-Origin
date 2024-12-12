package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.repository.PlaylistRepository

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor{
    override suspend fun addPlaylist(newplaylist: Playlist) {
        repository.addPlaylist(newplaylist)
    }
}