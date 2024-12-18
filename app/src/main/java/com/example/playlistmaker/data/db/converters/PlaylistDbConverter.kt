package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.entity.Playlist

class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(0, playlist.name, playlist.definition,
                               playlist.path, playlist.trackIdList, playlist.count
        )
    }
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(playlist.playlistId, playlist.name, playlist.definition,
            playlist.path, playlist.trackIdList, playlist.count)
    }
}