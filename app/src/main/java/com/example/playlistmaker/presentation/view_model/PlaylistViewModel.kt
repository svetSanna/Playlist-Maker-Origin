package com.example.playlistmaker.presentation.view_model

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.use_case.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlist: Playlist
) : ViewModel() {
    private var tracks = MutableLiveData<List<Track>?>() // список треков данного плейлиста

    init {
        loadTracks(playlist.playlistId)
    }

    fun loadTracks(playlistId: Int) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistInteractor.getTracksInPlaylist(playlistId)
                    .collect() {pair ->
                        val foundTracks = pair.first
                        if (foundTracks != null) {
                            tracks.postValue(foundTracks)
                        }
                    }
            }
        }
    }
    fun getTracksMutableData(): MutableLiveData<List<Track>?> {
        return tracks
    }

    fun deleteTrackFromPlaylist(item: Track, playlistId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistInteractor.deleteTrackFromPlaylist(item, playlistId)
                loadTracks(playlistId)
            }
        }
    }

    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistInteractor.deletePlaylist(playlistId)
            }
        }
    }
}