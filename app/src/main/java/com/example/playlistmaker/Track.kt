package com.example.playlistmaker

import android.os.Parcel
import android.os.Parcelable


// Класс для хранения информации об одном треке
data class Track(
    val trackId: Int, // идентификатор трека
    var trackName: String, // Название композиции
    var artistName: String, // Имя исполнителя
    var trackTimeMillis: Long, // Продолжительность трека   // var trackTime: String, // Продолжительность трека
    var artworkUrl100: String // Ссылка на изображение обложки
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}