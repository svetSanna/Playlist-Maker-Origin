package com.example.playlistmaker.data.repository

import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Задача этой реализации — получить список треков, используя сетевой клиент, и вернуть его в виде List<Track>:
class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override suspend fun searchTrack(str: String): Flow<Resource<List<Track>>> = flow { //<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(str))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(R.string.something_went_wrong.toString()))
            }
            200 -> {
                with(response as TrackSearchResponse) {
                    val results = response.results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    emit(Resource.Success(results))
                }
            }
            else -> {
                emit(Resource.Error(R.string.network_err.toString()))
            }
        }
    }
}
