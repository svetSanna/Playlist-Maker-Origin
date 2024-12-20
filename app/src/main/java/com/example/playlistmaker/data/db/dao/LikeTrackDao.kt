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
    @Insert(entity = LikeTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertLikeTrack(likeTrack: LikeTrackEntity)

    // метод @Delete для удаления трека из таблицы избранных треков;
    @Delete(entity = LikeTrackEntity::class)
    fun deleteLikeTrack(likeTrack: LikeTrackEntity)

    // метод @Query для получения списка со всеми треками, добавленными в избранное;
    @Query("SELECT * FROM like_track_entity")
    fun getLikeTracks(): List<LikeTrackEntity>

    // метод @Query для получения списка со всеми треками, добавленными в избранное;
    @Query("SELECT * FROM like_track_entity ORDER BY timeOfLikeSec DESC")
    fun getLikeTracksByTime(): List<LikeTrackEntity>

    // метод @Query для получения списка идентификаторов всех треков, которые добавлены в избранное.
    @Query("SELECT trackId FROM like_track_entity")
    fun getLikeTracksId(): List<Int>

    // метод @Query для получения трека по Id (должен один быть получен или null)
    @Query("SELECT * FROM like_track_entity WHERE trackId = (:id)")
    fun getLikeTrack(id: Int): LikeTrackEntity?

    // метод @Query для удаления трека по Id
    @Query("DELETE FROM like_track_entity WHERE trackId = (:id)")
    fun deleteLikeTrackById(id: Int)
}