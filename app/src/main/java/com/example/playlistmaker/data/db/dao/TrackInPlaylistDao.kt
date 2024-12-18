package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {
    // метод @Insert для добавления трека в таблицу
    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack(track: TrackInPlaylistEntity)

    // метод @Delete для удаления трека из таблицы
    @Delete(entity = TrackInPlaylistEntity::class)
    fun deleteTrack(track: TrackInPlaylistEntity)

    // метод @Query для получения списка со всеми треками, которые есть в каких-либо плейлистах
    @Query("SELECT * FROM track_in_playlist_entity")
    fun getTracks(): List<TrackInPlaylistEntity>

    // метод @Query для получения списка со всеми треками из таблицы по порядку: последние пришли, первыми ушли
    @Query("SELECT * FROM track_in_playlist_entity ORDER BY timeOfLikeSec DESC")
    fun getTracksByTime(): List<TrackInPlaylistEntity>

    // метод @Query для получения списка идентификаторов всех треков, которые добавлены в плейлисты
    @Query("SELECT trackId FROM track_in_playlist_entity")
    fun getTracksId(): List<Int>

    // метод @Query для получения трека по Id (должен один быть получен или null)
    @Query("SELECT * FROM track_in_playlist_entity WHERE trackId = (:id)")
    fun getTrack(id: Int): TrackInPlaylistEntity?

    // метод @Query для удаления трека по Id
    @Query("DELETE FROM track_in_playlist_entity WHERE trackId = (:id)")
    fun deleteTrackById(id: Int)
}