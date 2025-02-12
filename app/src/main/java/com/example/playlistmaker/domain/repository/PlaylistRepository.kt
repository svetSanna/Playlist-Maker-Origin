package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    //suspend fun addPlaylist(playList: PlaylistEntity) // метод для добавления плейлиста в БД
    suspend fun addPlaylist(playList: Playlist) // метод для добавления плейлиста в БД
    suspend fun deletePlaylist(playListId: Int) // метод для удаления плейлиста из БД
    suspend fun getPlaylists() : Flow<Resource<List<Playlist>>> // метод получения списка со всеми плейлистами
   // suspend fun updateTrackIdListByPlaylistId(newTrackIdList: String, identificator: Int)// метод для изменения значения в столбце trackIdList для плейлиста с заданным Id
    suspend fun addTrackToPlaylist(track: Track, playlistId: Int)// метод для добавления трека Track а список идентификаторов треков для плейлиста с заданным Id
    suspend fun getTracksInPlaylist(playlistId: Int) : Flow<Resource<List<Track>>>// метод для получения списка треков для плейлиста по идентификатору плейлиста
    suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Int) // удалить трек из плейлиста
    suspend fun editPlaylist(playlistId: Int, path: String?, title: String, definition: String?) // редактировать плейлист с идентификатором playlistId
}

