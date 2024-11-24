package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.LikeTrackListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class LikeTrackListInteractorImpl(private val repository: LikeTrackListRepository) : LikeTrackListInteractor {
    override suspend fun getLikeTrackList(): Flow<Pair<List<Track>?, String?>> {
        return repository.getLikeTrackList().map {result ->
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

    override suspend fun deleteTrackFromLikeTrackList(track: Track){
        repository.deleteTrackFromLikeTrackList(track)
    }

    override suspend fun addTrackToLikeTrackList(track: Track){
        repository.addTrackToLikeTrackList(track)
    }

    /*override suspend fun onFavoriteClicked(track: Track) // нажатие на кнопку лайк
    {
        if(isFavorite(track.trackId)){
            repository.deleteTrackFromLikeTrackList(track)
        }
        else
            repository.addTrackToLikeTrackList(track)
    }
*/
    override suspend fun isFavorite(trackId: Int) : Boolean{
        // если трек найден в бд по Id трека, то он избранный
       return (repository.getLikeTrack(trackId) != null)
    }
}

/*
class GetLikeTrackListUseCase(private val repository: LikeTrackListRepository) {
    suspend operator fun invoke(): Flow<Pair<List<Track>?, String?>> {
        return repository.getLikeTrackList().map {result ->
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
 */