package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteTracksViewModel(private val number: Int)  : ViewModel(){
    private val liveData = MutableLiveData(number)
    fun observeFavoriteTracks(): LiveData<Int> = liveData
}