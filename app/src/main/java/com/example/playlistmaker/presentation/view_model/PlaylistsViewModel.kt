package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistsViewModel(private val number_playlist: Int)  : ViewModel() {
    private val liveData = MutableLiveData(number_playlist)
    fun observePlaylists(): LiveData<Int> = liveData
}
