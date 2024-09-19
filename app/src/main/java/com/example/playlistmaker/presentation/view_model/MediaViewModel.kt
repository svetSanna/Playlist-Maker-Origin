package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.use_case.MediaPlayerInteractor

class MediaViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {
    fun preparePlayer(url: String?) {
        mediaPlayerInteractor.preparePlayer(url)
    }

    fun playbackControl() {
        mediaPlayerInteractor.playbackControl()
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
    }

    fun onDestroymediaPlayer() {
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
                        Creator.provideMediaPlayerInteractor(activity)
                    ) as T
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