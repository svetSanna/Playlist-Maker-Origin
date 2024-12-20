package com.example.playlistmaker.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Int, // идентификатор плейлиста
    var name: String,    // Название плейлиста, добавила уникальность, хотя по заданию не требуется
    var definition: String?,  // Описание плейлиста
    var path: String?,        // Путь к файлу изображения для обложки, который был скопирован в хранилище приложения при создании плейлиста
    var trackIdList: String?, // Список идентификаторов треков, которые будут добавляться в этот плейлист. Можете использовать Gson для преобразования списка в строку, которую можно сохранить в базу.
    // В момент создания плейлиста этот список будет пустым.
    var count: Int           // Количество треков, добавленных в плейлист
) : Parcelable

