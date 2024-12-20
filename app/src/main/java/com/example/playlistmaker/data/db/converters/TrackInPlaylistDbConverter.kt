package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.LikeTrackEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistEntity
import com.example.playlistmaker.domain.entity.Track
import java.time.Instant

class TrackInPlaylistDbConverter {
    fun map(track: Track): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
            track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100,
            track.collectionName, track.releaseDate, track.primaryGenreName,
            track.country, track.previewUrl, Instant.now().getEpochSecond()
        )
    }

    fun map(track: TrackInPlaylistEntity): Track {
        return Track(
            track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100,
            track.collectionName, track.releaseDate, track.primaryGenreName,
            track.country, track.previewUrl
        )
    }
}
