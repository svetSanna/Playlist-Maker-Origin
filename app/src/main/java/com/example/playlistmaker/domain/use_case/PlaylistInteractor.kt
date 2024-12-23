package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(newplaylist: Playlist) // добавить плейлист в БД
    suspend fun getPlaylistsList() : Flow<Pair<List<Playlist>?, String?>> // получить список плейлистов
    suspend fun addTrackToPlaylist(track: Track, playlistId: Int) // добавить трек track в плейлист, playlistId - идентификатор плейлиста
    suspend fun getTracksInPlaylist(playlistId: Int):  Flow<Pair<List<Track>?, String?>>//Flow<Resource<List<Track>>> // получить список треков для плейлиста с заданным Id
    suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Int) // удалить трек track из плейлиста с идентификатором playlistId

}