package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.use_case.PlaylistInteractor

class EditPlaylistViewModel(playlistInteractor: PlaylistInteractor, playlist: Playlist) :
    NewPlaylistViewModel(playlistInteractor) {

   /* private var tracks = MutableLiveData<List<Track>?>() // список треков данного плейлиста

    init {
        loadTracks(playlist.playlistId)
    }
*/
    fun savePlaylist(path: String?, title: String, definition: String) {

    }


}