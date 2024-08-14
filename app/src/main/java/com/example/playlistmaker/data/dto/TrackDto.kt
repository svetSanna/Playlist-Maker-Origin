package com.example.playlistmaker.data.dto

data class TrackDto(val trackId: Int, // идентификатор трека
                    var trackName: String, // Название композиции
                    var artistName: String, // Имя исполнителя
                    var trackTimeMillis: Long, // Продолжительность трека   // var trackTime: String, // Продолжительность трека
                    var artworkUrl100: String, // Ссылка на изображение обложки
                    var collectionName: String, // Название альбома (если его нет, то эту информацию на экране не показываем)
                    var releaseDate: String = "", // Год релиза трека
                    var primaryGenreName: String = "", // Жанр трека
                    var country: String = "", // Страна исполнителя
                    var previewUrl: String = "" //  Ссылка на отрывок трека в формате String
 )