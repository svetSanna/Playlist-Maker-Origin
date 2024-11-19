package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTrackListUseCase(private val repository: TrackRepository) {

    suspend operator fun invoke(str: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTrack(str).map {result ->
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
}

