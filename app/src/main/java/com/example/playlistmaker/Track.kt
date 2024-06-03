package com.example.playlistmaker


// Класс для хранения информации об одном треке
data class Track (
    var trackName: String, // Название композиции
    var artistName: String, // Имя исполнителя
    var trackTimeMillis: Long, // Продолжительность трека
   // var trackTime: String, // Продолжительность трека
    var artworkUrl100: String // Ссылка на изображение обложки
)