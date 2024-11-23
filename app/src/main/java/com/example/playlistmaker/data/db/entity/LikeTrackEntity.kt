package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

@Entity(tableName = "like_track_entity")
data class LikeTrackEntity(
    @PrimaryKey
    val trackId: Int, // идентификатор трека - первичный ключ
    var trackName: String, // Название композиции
    var artistName: String, // Имя исполнителя
    var trackTimeMillis: Long, // Продолжительность трека   // var trackTime: String, // Продолжительность трека
    var artworkUrl100: String, // Ссылка на изображение обложки для элемента списка и плейера
    var collectionName: String, // Название альбома (если его нет, то эту информацию на экране не показываем)
    var releaseDate: String = "", // Год релиза трека
    var primaryGenreName: String = "", // Жанр трека
    var country: String = "", // Страна исполнителя
    var previewUrl: String = "", //  Ссылка на отрывок трека в формате String
    var timeOfLikeSec: Long // время добавления трека в избранное (в секундах от начала эпохи UNIX - 1970-01-01 00:00:00 UTC)
)