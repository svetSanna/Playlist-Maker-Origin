package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.data.db.entity.LikeTrackEntity
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface LikeTrackListInteractor {
   // suspend fun onFavoriteClicked(track: Track) // нажатие на кнопку лайк
    suspend fun getLikeTrackList() : Flow<Pair<List<Track>?, String?>> // метод получения списка со всеми треками, добавленными в избранное.
    suspend fun deleteTrackFromLikeTrackList(track: Track) // удалить трек из списка избранных
    suspend fun addTrackToLikeTrackList(track: Track) // добавить трек в список избранных
    suspend fun isFavorite(trackId: Int) : Boolean // является ли трек избранным
}