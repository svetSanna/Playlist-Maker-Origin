package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.LikeTrackEntity
import com.example.playlistmaker.domain.entity.Track
import java.time.Instant

class LikeTrackDbConverter {
    fun map(track: Track): LikeTrackEntity{
        return LikeTrackEntity(track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100,
            track.collectionName, track.releaseDate, track.primaryGenreName,
            track.country, track.previewUrl, Instant.now().getEpochSecond()
        )
    }
    fun map(track: LikeTrackEntity): Track{
        return Track(track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100,
            track.collectionName, track.releaseDate, track.primaryGenreName,
            track.country, track.previewUrl)
    }
}