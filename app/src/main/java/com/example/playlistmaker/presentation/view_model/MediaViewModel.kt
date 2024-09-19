package com.example.playlistmaker.presentation.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.use_case.MediaPlayerInteractor
import com.example.playlistmaker.presentation.state.MediaPlayerState

class MediaViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {
    private val state = MutableLiveData<MediaPlayerState>()
    fun getMediaPlayerState() : LiveData<MediaPlayerState> = state
    fun preparePlayer(url: String?) {
        Log.i("MyTest", "MediaViewModel.preparePlayer")
        mediaPlayerInteractor.preparePlayer(url)
    }

    fun playbackControl() {
        Log.i("MyTest", "MediaViewModel.playbackControl")
        mediaPlayerInteractor.playbackControl()
    }

    fun pausePlayer() {
        Log.i("MyTest", "MediaViewModel.pausePlayer")
        mediaPlayerInteractor.pausePlayer()
    }

    fun onDestroymediaPlayer() {
        Log.i("MyTest", "MediaViewModel.onDestroymediaPlayer")
        mediaPlayerInteractor.onDestroy()
    }


    //private val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor(this)

    // фабрика нужна,если для создания ViewModel необходимо в конструктор передать параметр либо ссылку на активити или application
    companion object {
        fun getMediaViewModelfactory() : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                    ): T {
                    val activity = checkNotNull(extras[APPLICATION_KEY])

                    return MediaViewModel(
                        Creator.provideMediaPlayerInteractor()//activity)
                    ) as T
                    Log.i("MyTest", "MediaViewModel.getMediaViewModelFactory")
                }
            }
    }



    /* companion object {
        fun getMediaViewModelfactory() : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MediaViewModel(
                        Creator.provideMediaPlayerInteractor(this)
                    ) as T
                }
            }
    }*/
}