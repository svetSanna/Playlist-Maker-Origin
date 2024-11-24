package com.example.playlistmaker.presentation.view_model
// работа с медиаплейером, экран плейера

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.use_case.LikeTrackListInteractorImpl
import com.example.playlistmaker.domain.use_case.MediaPlayerInteractor
import com.example.playlistmaker.presentation.state.LikeButtonState
import com.example.playlistmaker.presentation.state.MediaPlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor,
                     private val url: String?,
                     private val likeTrackListInteractor: LikeTrackListInteractorImpl) : ViewModel() {
    private var timerJob: Job? = null //

    private val mediaPlayerState = MutableLiveData<MediaPlayerState>() // состояние медиаплейера
    private val likeButtonState = MutableLiveData<LikeButtonState>() // состояние кнопки лайк
    init{
        preparePlayer(url)
    }

    fun getMediaPlayerState(): LiveData<MediaPlayerState> {
        return mediaPlayerState
    }
    fun getLikeButtonState(): LiveData<LikeButtonState> {
        return likeButtonState
    }

    fun preparePlayer(url: String?) {
        mediaPlayerInteractor.preparePlayer(url)
        mediaPlayerState.postValue(MediaPlayerState.Prepared)
    }

    fun playbackControl() {
        if (mediaPlayerInteractor.isStateIsPlaying())
            pausePlayer()
        else
            startPlayer()
    }

    fun startPlayer(){
        mediaPlayerInteractor.startPlayer()
        mediaPlayerState.postValue(MediaPlayerState.Playing)
        startTimer()
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        timerJob?.cancel()
        mediaPlayerState.postValue(MediaPlayerState.Paused)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.isStateIsPlaying()) {
                delay(300L)
                mediaPlayerState.postValue(MediaPlayerState.Playing)
            }
            mediaPlayerState.postValue(MediaPlayerState.Prepared)
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
    fun onFavoriteClicked(track: Track){
    // нажатие на кнопку лайк
        viewModelScope.launch{
            if(likeTrackListInteractor.isFavorite(track.trackId)){
                likeTrackListInteractor.deleteTrackFromLikeTrackList(track)
                likeButtonState.postValue(LikeButtonState.Liked)
            }
            else{
                likeTrackListInteractor.addTrackToLikeTrackList(track)
                likeButtonState.postValue(LikeButtonState.Unliked)
            }
            //likeTrackListInteractor.onFavoriteClicked(track)
        }
    }
}