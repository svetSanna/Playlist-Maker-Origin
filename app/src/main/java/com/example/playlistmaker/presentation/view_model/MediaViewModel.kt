package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.use_case.MediaPlayerInteractor
import com.example.playlistmaker.presentation.state.MediaPlayerState

class MediaViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor, private val url: String?) : ViewModel() {
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
        mediaPlayerInteractor.playbackControl()
        if (mediaPlayerInteractor.isStateIsPlaying())
            state.postValue(MediaPlayerState.Playing)
        else
            state.postValue(MediaPlayerState.Paused)
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        state.postValue(MediaPlayerState.Paused)
    }

    fun onDestroyMediaPlayer() {
        mediaPlayerInteractor.onDestroy()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.getCurrentPosition()
    }

    // фабрика нужна,если для создания ViewModel необходимо в конструктор передать параметр либо ссылку на активити или application
    companion object {
        fun getMediaViewModelfactory(url: String?): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return MediaViewModel(
                        Creator.provideMediaPlayerInteractor(), url//activity)
                    ) as T
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        onDestroyMediaPlayer()
    }

}