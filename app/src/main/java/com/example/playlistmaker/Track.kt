package com.example.playlistmaker

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


// Класс для хранения информации об одном треке
@Parcelize
data class Track(
    val trackId: Int, // идентификатор трека
    var trackName: String, // Название композиции
    var artistName: String, // Имя исполнителя
    var trackTimeMillis: Long, // Продолжительность трека   // var trackTime: String, // Продолжительность трека
    var artworkUrl100: String, // Ссылка на изображение обложки
    var collectionName: String, // Название альбома (если его нет, то эту информацию на экране не показываем)
    var releaseDate: String = "", // Год релиза трека
    var primaryGenreName: String = "", // Жанр трека
    var country: String = "" // Страна исполнителя
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}


