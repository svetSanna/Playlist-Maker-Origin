package com.example.playlistmaker.ui.MediaActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.TRACK
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.mapper.SimpleDateFormatMapper


class MediaActivity : AppCompatActivity() {

    //private var mediaPlayer = MediaPlayer()
    private var getMediaPlayerUseCase = Creator.provideGetMediaPlayerUseCase()
    private var mediaPlayer = getMediaPlayerUseCase()

    companion object {
        private const val STATE_DEFAULT = 0 // освобожден
        private const val STATE_PREPARED = 1 // подготовлен
        private const val STATE_PLAYING = 2 // воспроизводится
        private const val STATE_PAUSED = 3 // пауза
        private const val TIME_DEBOUNCE =
            400L // время, через которое будет обновляться поле, показывающее, сколько времени от начала отрывка проиграно в формате
    }

    private var playerState = STATE_DEFAULT // cостояние плейера

    private var url: String? = ""

    private val handlerMain = Handler(Looper.getMainLooper())

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        // кнопка "Назад"
        val buttonBackMedia = findViewById<Toolbar>(R.id.toolbar)
        buttonBackMedia.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // кнопка "Play"/"Pause"
        val buttonPlayPause = findViewById<ImageView>(R.id.button_media_play_pause)

        // получаем данные трека из Intent
        var item: Track? = getIntent().getParcelableExtra(TRACK)

        // раскладываем эти данные по соответствующим вьюшкам
        var ivTrackImage: ImageView = findViewById(R.id.track_image)
        var tvTrackName: TextView = findViewById(R.id.track_name_data)
        var tvArtistName: TextView = findViewById(R.id.artist_name_data)
        var tvTrackTime: TextView = findViewById(R.id.track_time_millis_data) // длительность
        var tvCollectionName: TextView = findViewById(R.id.collection_name_data)
        var tvReleaseDate: TextView = findViewById(R.id.release_date_data)
        var tvPrimaryGenreName: TextView = findViewById(R.id.primary_genre_name_data)
        var tvCountry: TextView = findViewById(R.id.country_data)

        if (item != null) {
            Glide.with(this)
                .load(item.getCoverArtwork())
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .transform(RoundedCorners(8))//(R.dimen.corner_radius_8))
                .into(ivTrackImage)

            tvTrackName.text = item.trackName
            tvArtistName.text = item.artistName
            //tvTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
            tvTrackTime.text = SimpleDateFormatMapper.map(item.trackTimeMillis)
            tvCollectionName.text = item.collectionName
            tvReleaseDate.text = item.releaseDate.substring(0, 4)
            tvPrimaryGenreName.text = item.primaryGenreName
            tvCountry.text = item.country
        }

        // ссылка на отрывок
        url = item?.previewUrl

        // подготавливаем плейер
        preparePlayer()

        buttonPlayPause.setOnClickListener {
            playbackControl()
        }
    }

    private val timeTrackRunnable = object : Runnable {
        override fun run() {
            var timeTrack = findViewById<TextView>(R.id.time)
            // обновляем время
            // timeTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            timeTrack.text = SimpleDateFormatMapper.map(mediaPlayer.currentPosition)
            handlerMain?.postDelayed(this, TIME_DEBOUNCE)
        }
    }

    // подготовка плейера
    private fun preparePlayer() {
        // кнопка "Play"/"Pause"
        val buttonPlayPause = findViewById<ImageView>(R.id.button_media_play_pause)
        // отображение времени трека
        var timeTrack = findViewById<TextView>(R.id.time)

        mediaPlayer.setDataSource(url) // установить источник
        mediaPlayer.prepareAsync() // подготовка
        mediaPlayer.setOnPreparedListener {
            buttonPlayPause.setImageResource(R.drawable.button_media_play)
            playerState = STATE_PREPARED
            timeTrack.text = "00:00"
        }
        mediaPlayer.setOnCompletionListener {// отслеживание завершения воспроизведения
            buttonPlayPause.setImageResource(R.drawable.button_media_play)
            playerState = STATE_PREPARED
            timeTrack.text = "00:00"
            handlerMain?.removeCallbacks(timeTrackRunnable) // удаляем из очереди все сообщения Runnable, чтобы таймер не обновлялся
        }
    }

    // запустить плейер
    private fun startPlayer() {
        // кнопка "Play"/"Pause"
        val buttonPlayPause = findViewById<ImageView>(R.id.button_media_play_pause)

        mediaPlayer.start()
        buttonPlayPause.setImageResource(R.drawable.button_media_pause)
        playerState = STATE_PLAYING

        handlerMain?.postDelayed(
            timeTrackRunnable,
            TIME_DEBOUNCE
        )  // ставим в очередь обновление таймера
    }

    // поставить плейер на паузу
    private fun pausePlayer() {
        // кнопка "Play"/"Pause"
        val buttonPlayPause = findViewById<ImageView>(R.id.button_media_play_pause)

        mediaPlayer.pause()
        buttonPlayPause.setImageResource(R.drawable.button_media_play)
        playerState = STATE_PAUSED

        handlerMain?.removeCallbacks(timeTrackRunnable) // удаляем из очереди все сообщения Runnable, чтобы таймер не обновлялся
    }

    // функция вызывается при нажатии на кнопку Play/Pause
    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    // Активити на паузу
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    // Активити закрывается
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}