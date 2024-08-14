package com.example.playlistmaker.domain.consumer

import com.example.playlistmaker.domain.entity.Track

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}

/*interface Consumer {
    fun consume(foundTracks: List<Track>)
}*/