package com.example.playlistmaker.data.repository

import android.app.Activity
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getString
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.presentation.mapper.SimpleDateFormatMapper

/*class MediaPlayerRepositoryImpl : MediaPlayerRepository {
    override fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }
}*/ //p1
class MediaPlayerRepositoryImpl(private val activity: Activity) : MediaPlayerRepository { //p1
    private var mediaPlayer = MediaPlayer()

    companion object {
        private const val STATE_DEFAULT = 0 // освобожден
        private const val STATE_PREPARED = 1 // подготовлен
        private const val STATE_PLAYING = 2 // воспроизводится
        private const val STATE_PAUSED = 3 // пауза
        private const val TIME_DEBOUNCE =
            400L // время, через которое будет обновляться поле, показывающее, сколько времени от начала отрывка проиграно в формате
    }

    private var playerState = STATE_DEFAULT // cостояние плейера

    private val handlerMain = Handler(Looper.getMainLooper())

    private var timeTrack = activity.findViewById<TextView>(R.id.time)//p6
    init {
        timeTrack.text = getString(activity, R.string.time_00_00)//.toString() //"00:00" //p6
    }

    //private var url: String? = ""
    private val timeTrackRunnable = object : Runnable {
        override fun run() {
            var timeTrack = activity.findViewById<TextView>(R.id.time)
            // обновляем время
            //timeTrack.text = SimpleDateFormatMapper.map(mediaPlayer.currentPosition) //p1
            timeTrack.text = SimpleDateFormatMapper.map(getCurrentPosition()) //p1
            handlerMain?.postDelayed(this, TIME_DEBOUNCE)
        }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun preparePlayer(url: String?) {
        // подготовка плейера
        // кнопка "Play"/"Pause"
        val buttonPlayPause = activity.findViewById<ImageView>(R.id.button_media_play_pause)
        // отображение времени трека

        mediaPlayer.setDataSource(url) // установить источник
        mediaPlayer.prepareAsync() // подготовка
        mediaPlayer.setOnPreparedListener {
            buttonPlayPause.setImageResource(R.drawable.button_media_play)
            playerState = STATE_PREPARED
            timeTrack.text = getString(activity, R.string.time_00_00)
        }
        mediaPlayer.setOnCompletionListener {// отслеживание завершения воспроизведения
            buttonPlayPause.setImageResource(R.drawable.button_media_play)
            playerState = STATE_PREPARED
            timeTrack.text = getString(activity, R.string.time_00_00)
            handlerMain?.removeCallbacks(timeTrackRunnable) // удаляем из очереди все сообщения Runnable, чтобы таймер не обновлялся
        }
        /*
        // подготовка плейера
        // кнопка "Play"/"Pause"
        val buttonPlayPause = activity.findViewById<ImageView>(R.id.button_media_play_pause)
        // отображение времени трека
        //var timeTrack = activity.findViewById<TextView>(R.id.time)
        // timeTrack.text = R.string.time_00_00.toString() //"00:00" //p6

        mediaPlayer.setDataSource(url) // установить источник
        mediaPlayer.prepareAsync() // подготовка
        mediaPlayer.setOnPreparedListener {
            buttonPlayPause.setImageResource(R.drawable.button_media_play)
            playerState = STATE_PREPARED
            timeTrack.text = getString(activity, R.string.time_00_00)//R.string.time_00_00.toString()//"00:00" //p5
        }
        mediaPlayer.setOnCompletionListener {// отслеживание завершения воспроизведения
            buttonPlayPause.setImageResource(R.drawable.button_media_play)
            playerState = STATE_PREPARED
            timeTrack.text = getString(activity, R.string.time_00_00)//R.string.time_00_00.toString()//"00:00" //p5
            handlerMain?.removeCallbacks(timeTrackRunnable) // удаляем из очереди все сообщения Runnable, чтобы таймер не обновлялся
        }
         */

    }

    override fun startPlayer() {
        // запустить плейер        // кнопка "Play"/"Pause"
        val buttonPlayPause = activity.findViewById<ImageView>(R.id.button_media_play_pause)

        mediaPlayer.start()
        buttonPlayPause.setImageResource(R.drawable.button_media_pause)
        playerState = STATE_PLAYING

        handlerMain?.postDelayed(
            timeTrackRunnable,
            TIME_DEBOUNCE
        )  // ставим в очередь обновление таймера
    }

    override fun pausePlayer() {
        // кнопка "Play"/"Pause"
        val buttonPlayPause = activity.findViewById<ImageView>(R.id.button_media_play_pause)

        mediaPlayer.pause()
        buttonPlayPause.setImageResource(R.drawable.button_media_play)
        playerState = STATE_PAUSED

        handlerMain?.removeCallbacks(timeTrackRunnable) // удаляем из очереди все сообщения Runnable, чтобы таймер не обновлялся
    }

    override fun playbackControl() {
        // функция вызывается при нажатии на кнопку Play/Pause
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onDestroy() {
        mediaPlayer.release()
    }
}