package com.example.playlistmaker.presentation.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.use_case.LikeTrackListInteractor
import com.example.playlistmaker.domain.use_case.LikeTrackListInteractorImpl
import com.example.playlistmaker.presentation.state.LikeTracksScreenState
import com.example.playlistmaker.presentation.state.SearchScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LikeTracksViewModel(private val likeTrackListInteractor: LikeTrackListInteractor,
                          private val context: Context
) : ViewModel() {

    private val state = MutableLiveData<LikeTracksScreenState>()

    init {
        loadData()
    }
    fun getScreenState(): LiveData<LikeTracksScreenState> {
        return state
    }
    fun loadData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                likeTrackListInteractor.getLikeTrackList()
                    .collect() { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        when {
            errorMessage != null -> {
                val error = LikeTracksScreenState.Error(
                    message = context.getString(
                        R.string.your_mediateka_is_empty
                    )
                )
                state.postValue(error)
            }

            else -> {
                val content = LikeTracksScreenState.Content(data = tracks)
                state.postValue(content)
            }
        }
    }
}