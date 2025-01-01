package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor{
    override suspend fun addPlaylist(newplaylist: Playlist) {
        repository.addPlaylist(newplaylist)
    }
    override suspend fun getPlaylistsList() :  Flow<Pair<List<Playlist>?, String?>> {
        return repository.getPlaylists().map {result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Int) {
       repository.addTrackToPlaylist(track, playlistId)
    }

    override suspend fun getTracksInPlaylist(playlistId: Int): Flow<Pair<List<Track>?, String?>> {
        return repository.getTracksInPlaylist(playlistId).map {result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Int) {
        repository.deleteTrackFromPlaylist(track, playlistId)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        repository.deletePlaylist(playlistId)
    }

    override suspend fun editPlaylist(
        playlistId: Int,
        path: String?,
        title: String,
        definition: String?
    ) {
        repository.editPlaylist(playlistId, path, title, definition)
    }
}