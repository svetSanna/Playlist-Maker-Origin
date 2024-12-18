package com.example.playlistmaker.data.repository

import com.example.playlistmaker.R
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.data.db.converters.TrackInPlaylistDbConverter
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackInPlaylistDbConverter: TrackInPlaylistDbConverter
) : PlaylistRepository {
   /* override suspend fun addPlaylist(playlist: PlaylistEntity) {
        appDatabase.playListDao().insertPlaylist(playlist)
    }*/
   override suspend fun addPlaylist(playlist: Playlist) {
       appDatabase.playListDao().insertPlaylist(playlistDbConverter.map(playlist))
   }
    override suspend fun deletePlaylist(playlist: PlaylistEntity) {
        appDatabase.playListDao().deletePlaylist(playlist)
    }
    override suspend fun getPlaylists(): Flow<Resource<List<Playlist>>> = flow {
        var playlists=  appDatabase.playListDao().getPlaylists()

        if (playlists.isEmpty())
            emit(Resource.Error(R.string.no_playlists.toString()))
        else
            emit(Resource.Success(convertListToList(playlists)))
    }
  /*  override suspend fun updateTrackIdListByPlaylistId(newTrackIdList: String, identificator: Int){
        appDatabase.playListDao().updateTrackIdListByPlaylistId(newTrackIdList, identificator)
    }*/
    private fun convertListToList(playlists: List<PlaylistEntity>)
            : List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist)}
    }
    override suspend fun addTrackIdToPlaylist(track: Track, playlistId: Int){
        var str = appDatabase.playListDao().getTrackIdList(playlistId)
        str += "," + track.trackId.toString()
        appDatabase.playListDao().updateTrackIdListByPlaylistId(str!!, playlistId)

        appDatabase.trackInPlaylistDao().insertTrack(trackInPlaylistDbConverter.map(track))
    }
}
