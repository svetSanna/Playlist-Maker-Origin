package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.data.db.entity.LikeTrackEntity
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface LikeTrackListRepository {
    suspend fun addTrackToLikeTrackList(track: LikeTrackEntity) // метод для добавления трека в избранное;
    suspend fun addTrackToLikeTrackList(track: Track) // метод для добавления трека в избранное;
    suspend fun deleteTrackFromLikeTrackList(track: LikeTrackEntity) // метод для удаления трека из избранного;
    suspend fun deleteTrackFromLikeTrackList(trackId: Int) // метод для удаления трека из избранного по Id
    suspend fun deleteTrackFromLikeTrackList(track: Track)
    suspend fun getLikeTrackList() : Flow<Resource<List<Track>>> // метод получения списка со всеми треками, добавленными в избранное.
    suspend fun getLikeTrack(trackId: Int) : Track? // метод получения списка треков по Id. Вообще-то должен получить один трек или пусто
    //suspend fun getLikeTrack(trackId: Int) : Flow<List<Track>> // метод получения списка треков по Id. Вообще-то должен получить один трек или пусто
}