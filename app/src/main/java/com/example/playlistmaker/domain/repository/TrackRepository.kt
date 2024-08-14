package com.example.playlistmaker.domain.repository


import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track

// некий интерактор сможет использовать этот интерфейс, чтобы получить список треков
// по поисковому запросу
/*interface TrackRepository {
    fun searchTrack(str: String): List<Track>
}*/

interface TrackRepository {
    fun searchTrack(str: String): Resource<List<Track>>
}

/* interface TrackRepository {
    fun searchTrack(str: String): List<Track>//Resource<List<Track>>
}*/