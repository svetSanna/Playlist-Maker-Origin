package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(newplaylist: Playlist) // добавить плейлист в БД
    suspend fun getPlaylistsList() : Flow<Pair<List<Playlist>?, String?>> // получить список плейлистов
}