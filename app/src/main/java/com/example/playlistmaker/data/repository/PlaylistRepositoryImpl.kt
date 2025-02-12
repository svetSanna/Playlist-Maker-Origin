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
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playListDao().insertPlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        var str = appDatabase.playListDao().getTrackIdList(playlistId)
        // удаляем плейлист
        appDatabase.playListDao().deletePlaylist(playlistId)

        if (!str.isNullOrBlank()) {
            // преобразуем список Id треков из строки в List
            val idTrackList: List<String> = str.split(',').toList()

            for (trackId in idTrackList) {
                // проверяем каждый трек, содержится ли он еще в каком=нибудь плейлисте.
                // Если нет - удаляем его из таблицы TrackInPlaylistEntity
                if (!trackId.isNullOrBlank())
                    checkAndDeleteUnnecessaryTrack(trackId.toInt())
            }
        }
    }

    override suspend fun getPlaylists(): Flow<Resource<List<Playlist>>> = flow {
        var playlists = appDatabase.playListDao().getPlaylists()

        if (playlists.isEmpty())
            emit(Resource.Error(R.string.no_playlists.toString()))
        else
            emit(Resource.Success(convertListToList(playlists)))
    }

    private fun convertListToList(playlists: List<PlaylistEntity>)
            : List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Int) {
        var str = appDatabase.playListDao().getTrackIdList(playlistId)
        if (str.isNullOrBlank()) str = track.trackId.toString()
        else str += "," + track.trackId.toString()

        appDatabase.playListDao().setTrackIdListByPlaylistId(str!!, playlistId)
        appDatabase.playListDao()
            .setCount((appDatabase.playListDao().getCount(playlistId) + 1), playlistId)

        appDatabase.trackInPlaylistDao().insertTrack(trackInPlaylistDbConverter.map(track))
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Int) {
        // получаем список идентификаторов треков данного плейлиста
        var str = appDatabase.playListDao().getTrackIdList(playlistId)
        if (!str.isNullOrBlank()) {
            // преобразуем его
            val idTrackList: MutableList<String> = str.split(',').toMutableList()

            // удаляем идентификатор трека
            idTrackList.remove(track.trackId.toString()) // если нет такого, то ничего не изменится

            // создаем список заново
            var newStr = ""
            for (i in 0..idTrackList.count() - 1) {
                if (!idTrackList[i].isNullOrBlank()) {
                    //count =
                    newStr += (idTrackList[i] + ",")
                }
            }

            // помещаем его в БД на место старого списка
            appDatabase.playListDao().setTrackIdListByPlaylistId(newStr, playlistId)
            // уменьшаем счетчик треков в плейлисте
            val count = appDatabase.playListDao().getCount(playlistId)
            appDatabase.playListDao().setCount(count - 1, playlistId)

            //удалить трек из таблицы TrackInPlaylistEntity, если его больше нет ни в одном списке
            checkAndDeleteUnnecessaryTrack(track.trackId)
        }
    }

    override suspend fun editPlaylist(
        playlistId: Int,
        path: String?,
        title: String,
        definition: String?
    ) {
        appDatabase.playListDao().editPlaylist(playlistId, path, title, definition)
    }

    private fun checkAndDeleteUnnecessaryTrack(trackId: Int) {
        // проверяем, в скольких плейлистах содержится данный трек, и удаляем его из таблицы
        // TrackInPlaylistEntity, если его больше нет ни в одном плейлисте
        var number: Int = 0
        for (playlist in appDatabase.playListDao().getPlaylists()) {
            val s = appDatabase.playListDao().getTrackIdList(playlist.playlistId)
            if (!s.isNullOrBlank()) {
                val idList: MutableList<String> = s!!.split(',').toMutableList()
                if (idList.contains(trackId.toString())) number++
            }
        }
        // если трека нет ни в одном списке, удаляем его из таблицы TrackInPlaylistEntity
        if (number == 0) appDatabase.trackInPlaylistDao().deleteTrackById(trackId)
    }

    override suspend fun getTracksInPlaylist(playlistId: Int): Flow<Resource<List<Track>>> = flow {
        var str = appDatabase.playListDao().getTrackIdList(playlistId)
        // получаем для данного плейлиста список идентификаторов треков
        if (str != null) {
            var trackList: MutableList<Track> = arrayListOf()
            var trackIdList: MutableList<Int> = arrayListOf()
            str.split(',').forEach { s ->
                if (!s.isNullOrBlank())
                    trackIdList.add(s.toInt())
            }
            // получаем по идентивикаторам треки
            trackIdList.forEach { id ->
                var track = appDatabase.trackInPlaylistDao().getTrack(id)
                if (track == null)
                    emit(Resource.Error(R.string.db_error.toString()))
                else trackList.add(trackInPlaylistDbConverter.map(track!!))
            }
            emit(Resource.Success(trackList))
        } else
            emit(Resource.Error(R.string.no_playlists.toString()))
    }
}
