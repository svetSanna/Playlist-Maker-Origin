package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistEntity

@Dao
interface PlayListDao {
    // метод @Insert для добавления плейлиста в таблицу;
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity)

    // метод @Delete для удаления плейлиста из таблицы
    @Delete(entity = PlaylistEntity::class)
    fun deletePlaylist(playlist: PlaylistEntity)

    // метод @Delete для удаления плейлиста из таблицы по идентификатору
    @Query("DELETE FROM play_list_entity WHERE playlistId =:playlistIdentificator")
    fun deletePlaylist(playlistIdentificator: Int)

    // метод @Query для получения списка со всеми плейлистами
    @Query("SELECT * FROM play_list_entity")
    fun getPlaylists(): List<PlaylistEntity>

    // метод @Query для изменения значения в столбце trackIdList для плейлиста с заданным Id
    @Query("UPDATE play_list_entity SET trackIdList = :newTrackIdList WHERE playlistId = :playlistIdentificator")
    fun setTrackIdListByPlaylistId(newTrackIdList: String, playlistIdentificator: Int)

    // метод @Query для изменения значения в столбце count для плейлиста с заданным Id
    @Query("UPDATE play_list_entity SET count = :newCount WHERE playlistId = :playlistIdentificator")
    fun setCount(newCount: Int, playlistIdentificator: Int)

    // метод @Query для получения значения count для плейлиста с заданным Id
    @Query("SELECT count FROM play_list_entity WHERE playlistId = :playlistIdentificator")
    fun getCount(playlistIdentificator: Int): Int

    // метод @Query для получения списка треков в плейлисте по идентификатору Id плейлиста
    @Query("SELECT trackIdList FROM play_list_entity WHERE playlistId = (:playlistIdentificator)")
    fun getTrackIdList(playlistIdentificator: Int): String?

    // метод @Query для изменения значения в столбце count для плейлиста с заданным Id
    @Query("UPDATE play_list_entity SET path = :newPath, name = :newTitle, definition = :newDefinition WHERE playlistId = :playlistIdentificator")
    fun editPlaylist(playlistIdentificator: Int, newPath: String?, newTitle: String, newDefinition: String?)

    // метод @Query для удаления трека по Id
    //@Query("DELETE FROM like_track_entity WHERE trackId = (:id)")
    //fun deleteLikeTrackById(id: Int)
}