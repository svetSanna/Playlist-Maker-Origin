package com.example.playlistmaker.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlayListEntity

interface PlayListDao {
    // метод @Insert для добавления плейлиста в таблицу;
    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayList(likeTrack: PlayListEntity)

    // метод @Delete для удаления плейлиста из таблицы
    @Delete(entity = PlayListEntity::class)
    fun deletePlayList(likeTrack: PlayListEntity)

    // метод @Query для получения списка со всеми плейлистами
    @Query("SELECT * FROM play_list_entity")
    fun getPlayLists(): List<PlayListEntity>

    // метод @Query для изменения значения в столбце trackIdList для плейлиста с заданным Id
    @Query("UPDATE play_list_entity SET trackIdList = :newTrackIdList WHERE playlistId = :identificator")
    fun updateTrackIdListByPlayListId(newTrackIdList: String, identificator: Int)


    // метод @Query для получения трека по Id (должен один быть получен или null)
    //@Query("SELECT * FROM like_track_entity WHERE trackId = (:id)")
    //fun getLikeTrack(id: Int): LikeTrackEntity?

    // метод @Query для удаления трека по Id
    //@Query("DELETE FROM like_track_entity WHERE trackId = (:id)")
    //fun deleteLikeTrackById(id: Int)
}