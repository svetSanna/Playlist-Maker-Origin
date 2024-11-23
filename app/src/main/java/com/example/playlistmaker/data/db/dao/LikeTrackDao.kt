package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.LikeTrackEntity

@Dao
interface LikeTrackDao {
    // метод @Insert для добавления трека в таблицу с избранными треками;
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikeTrack(likeTrack: LikeTrackEntity)

    // метод @Delete для удаления трека из таблицы избранных треков;
    @Delete(entity = LikeTrackEntity::class)
    suspend fun deleteLikeTrack(likeTrack: LikeTrackEntity)

    // метод @Query для получения списка со всеми треками, добавленными в избранное;
    @Query("SELECT * FROM like_track_entity")
    suspend fun getLikeTracks(): List<LikeTrackEntity>

    // метод @Query для получения списка со всеми треками, добавленными в избранное;
    @Query("SELECT * FROM like_track_entity ORDER BY timeOfLikeSec DESC")
    suspend fun getLikeTracksByTime(): List<LikeTrackEntity>

    // метод @Query для получения списка идентификаторов всех треков, которые добавлены в избранное.
    @Query("SELECT * FROM like_track_entity")
    suspend fun getLikeTracksId(): List<Int>

    // метод @Query для получения трека по Id (должен один быть получен или null)
    @Query("SELECT * FROM like_track_entity WHERE trackId = (:id)")
    suspend fun getLikeTrack(id: Int): LikeTrackEntity?
    //@Query("SELECT * FROM like_track_entity WHERE trackId = (:Id)")
    //suspend fun getLikeTrack(Id: Int): List<LikeTrackEntity>

    // метод @Query для удаления трека по Id
    @Query("DELETE FROM like_track_entity WHERE trackId = (:id)")
    fun deleteLikeTrackById(id: Int)
}