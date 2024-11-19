package com.example.playlistmaker.domain.repository


import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun searchTrack(str: String): Flow<Resource<List<Track>>>
}

