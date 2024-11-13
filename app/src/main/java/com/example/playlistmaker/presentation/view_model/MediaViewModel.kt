package com.example.playlistmaker.presentation.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.use_case.MediaPlayerInteractor
import com.example.playlistmaker.presentation.mapper.SimpleDateFormatMapper
import com.example.playlistmaker.presentation.state.MediaPlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor, private val url: String?) : ViewModel() {
    private var timerJob: Job? = null //

    private val state = MutableLiveData<MediaPlayerState>()
    init{
        preparePlayer(url)
    }

    fun getMediaPlayerState(): LiveData<MediaPlayerState> = state
    fun preparePlayer(url: String?) {
        mediaPlayerInteractor.preparePlayer(url)
        state.postValue(MediaPlayerState.Prepared)
    }

    fun playbackControl() {
        if (mediaPlayerInteractor.isStateIsPlaying())
            pausePlayer()
        else
            startPlayer()
    }

    fun startPlayer(){
        mediaPlayerInteractor.startPlayer()
        state.postValue(MediaPlayerState.Playing)
        startTimer()
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        timerJob?.cancel()
        state.postValue(MediaPlayerState.Paused)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.isStateIsPlaying()) {
                delay(300L)
                state.postValue(MediaPlayerState.Playing)
            }
        }
    }

    fun onDestroyMediaPlayer() {
        //Log.i("MyTest", "MediaViewModel.onDestroyMediaPlayer() ")
        mediaPlayerInteractor.onDestroy()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.getCurrentPosition()
    }

    override fun onCleared() {
        super.onCleared()
        //Log.i("MyTest", "MediaViewModel.onCleared() ")
        onDestroyMediaPlayer()
    }

    fun onPause(){
        //Log.i("MyTest", "MediaViewModel.onPause() ")
        pausePlayer()
    }
}