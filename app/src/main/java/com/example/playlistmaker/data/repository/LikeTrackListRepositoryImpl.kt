package com.example.playlistmaker.data.repository

import com.example.playlistmaker.R
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.TrackDbConverter
import com.example.playlistmaker.data.db.entity.LikeTrackEntity
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.LikeTrackListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LikeTrackListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
)  : LikeTrackListRepository {
    override suspend fun addTrackToLikeTrackList(track: LikeTrackEntity) {
        appDatabase.likeTrackDao().insertLikeTrack(track)
    }

    override suspend fun addTrackToLikeTrackList(track: Track) {
        appDatabase.likeTrackDao().insertLikeTrack(trackDbConverter.map(track))
    }

    override suspend fun deleteTrackFromLikeTrackList(track: LikeTrackEntity) {
        appDatabase.likeTrackDao().deleteLikeTrack(track)
    }

    override suspend fun deleteTrackFromLikeTrackList(trackId: Int) {
        appDatabase.likeTrackDao().deleteLikeTrackById(trackId)
    }
    override suspend fun deleteTrackFromLikeTrackList(track: Track) {
        appDatabase.likeTrackDao().deleteLikeTrack(trackDbConverter.map(track))
    }

    override suspend fun getLikeTrackList(): Flow<Resource<List<Track>>> = flow {
       val tracks = appDatabase.likeTrackDao().getLikeTracksByTime()

        if (tracks.isEmpty())
            emit(Resource.Error(R.string.your_mediateka_is_empty.toString()))
        else
            emit(Resource.Success(convertListToList(tracks)))
    }

    override suspend fun getLikeTrack(trackId: Int) : Track? {
       // возвращается трек по Id. Если такого нет, возвращается null
       var track = appDatabase.likeTrackDao().getLikeTrack(trackId)

       return if (track == null) null
       else trackDbConverter.map(track)
    }

    private fun convertListToList(tracks: List<LikeTrackEntity>)
    : List<Track> {
        return tracks.map { track -> trackDbConverter.map(track)}
    }
}