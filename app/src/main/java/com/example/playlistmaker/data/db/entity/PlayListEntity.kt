package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "play_list_entity", indices = [Index(value = ["name"], unique = true)])
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int, // идентификатор плейлиста
    var name: String,    // Название плейлиста, добавила уникальность, хотя по заданию не требуется
    var definition: String,  // Описание плейлиста
    var path: String,        // Путь к файлу изображения для обложки, который был скопирован в хранилище приложения при создании плейлиста
    var trackIdList: String, // Список идентификаторов треков, которые будут добавляться в этот плейлист. Можете использовать Gson для преобразования списка в строку, которую можно сохранить в базу.
                             // В момент создания плейлиста этот список будет пустым.
    var count: Int           // Количество треков, добавленных в плейлист
)