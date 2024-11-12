package com.example.playlistmaker.presentation.view_model

import android.os.Handler
import android.os.Looper
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
    /*companion object {
        private const val TIME_DEBOUNCE =
            300L // время, через которое будет обновляться поле, показывающее, сколько времени от начала отрывка проиграно в формате
    }*/
    private var timerJob: Job? = null //

    private val state = MutableLiveData<MediaPlayerState>()
    init{
        preparePlayer(url)
    }

    //var flag = true
    /*private val handlerMain = Handler(Looper.getMainLooper())

    private val timeTrackRunnable = object : Runnable {
        override fun run() {
            // обновляем время
            timeTrack.text = SimpleDateFormatMapper.map(viewModel.getCurrentPosition())

            handlerMain?.postDelayed(this, TIME_DEBOUNCE)
        }
    }*/

    fun getMediaPlayerState(): LiveData<MediaPlayerState> = state
    fun preparePlayer(url: String?) {
        mediaPlayerInteractor.preparePlayer(url)
        state.postValue(MediaPlayerState.Prepared)
    }

    fun playbackControl() {

       // mediaPlayerInteractor.playbackControl()
        if (mediaPlayerInteractor.isStateIsPlaying())
            pausePlayer()//state.postValue(MediaPlayerState.Playing)
        else
            startPlayer()//state.postValue(MediaPlayerState.Paused)
       // flag = !flag
    }

    fun startPlayer(){
        mediaPlayerInteractor.startPlayer()
        state.postValue(MediaPlayerState.Playing)
        startTimer() //
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        timerJob?.cancel() //
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
        mediaPlayerInteractor.onDestroy()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.getCurrentPosition()
    }

    override fun onCleared() {
        super.onCleared()
        onDestroyMediaPlayer()
    }

   /* fun onPause(){
        pausePlayer()
    }*/
}