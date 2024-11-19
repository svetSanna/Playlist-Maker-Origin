package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private var mediaPlayer : MediaPlayer) :
    MediaPlayerRepository {

    companion object {
        private const val STATE_DEFAULT = 0 // освобожден
        private const val STATE_PREPARED = 1 // подготовлен
        private const val STATE_PLAYING = 2 // воспроизводится
        private const val STATE_PAUSED = 3 // пауза
    }

    private var playerState = STATE_DEFAULT // cостояние плейера

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun preparePlayer(url: String?) {
        // подготовка плейера
        // кнопка "Play"/"Pause"
        mediaPlayer.setDataSource(url) // установить источник
        mediaPlayer.prepareAsync() // подготовка
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {// отслеживание завершения воспроизведения
            playerState = STATE_PREPARED
        }
    }

    override fun startPlayer() {
        // запустить плейер        // кнопка "Play"/"Pause"
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        // кнопка "Play"/"Pause"
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun onDestroy() {
        //mediaPlayer.release() - убрала потому что валилась программа при повороте
        mediaPlayer.reset()
    }

    override fun isStateIsPlaying(): Boolean {
        return (playerState == STATE_PLAYING)
    }
}