package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.use_case.PlaylistInteractorImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractorImpl) : ViewModel() {
    fun CreatePlaylist(path: String?, title: String, definition: String?){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistInteractor.addPlaylist(Playlist(title, definition, path, null, 0))
            }
        }
    }
}