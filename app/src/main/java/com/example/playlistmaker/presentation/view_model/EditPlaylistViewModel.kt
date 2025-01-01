package com.example.playlistmaker.presentation.view_model

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.use_case.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPlaylistViewModel(playlistInteractor: PlaylistInteractor, playlist: Playlist) :
    NewPlaylistViewModel(playlistInteractor) {

   /* private var tracks = MutableLiveData<List<Track>?>() // список треков данного плейлиста

    init {
        loadTracks(playlist.playlistId)
    }
*/
    fun savePlaylist(playlistId: Int, path: String?, title: String, definition: String?) {
       viewModelScope.launch {
           withContext(Dispatchers.IO) {
               playlistInteractor.editPlaylist(playlistId, path, title, definition)
           }
       }
    }
}