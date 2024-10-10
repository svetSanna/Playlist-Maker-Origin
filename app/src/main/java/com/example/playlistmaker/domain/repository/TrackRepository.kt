package com.example.playlistmaker.domain.repository


import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track

interface TrackRepository {
    fun searchTrack(str: String): Resource<List<Track>>
}

