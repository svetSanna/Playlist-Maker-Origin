package com.example.playlistmaker.presentation.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.use_case.PlaylistInteractor
import com.example.playlistmaker.presentation.state.PlaylistsScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor,
                         private val context: Context
)  : ViewModel() {

    private val state = MutableLiveData<PlaylistsScreenState>()
    init {
        loadData()
    }
    fun loadData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistInteractor.getPlaylistsList()
                    .collect() { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundPlaylists: List<Playlist>?, errorMessage: String?) {
        val playlists = mutableListOf<Playlist>()
        if (foundPlaylists != null) {
            playlists.addAll(foundPlaylists)
        }

        when {
            errorMessage != null -> {
                val error = PlaylistsScreenState.Error(
                    message = context.getString(
                        R.string.no_playlists
                    )
                )
                state.postValue(error)
            }

            else -> {
                val content = PlaylistsScreenState.Content(data = playlists)
                state.postValue(content)
            }
        }
    }

    fun getScreenState(): LiveData<PlaylistsScreenState> {
        return state
    }
}
