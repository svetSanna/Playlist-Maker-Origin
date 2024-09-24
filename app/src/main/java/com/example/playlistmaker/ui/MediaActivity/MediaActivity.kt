package com.example.playlistmaker.ui.MediaActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.TRACK
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.mapper.SimpleDateFormatMapper
import com.example.playlistmaker.presentation.state.MediaPlayerState
import com.example.playlistmaker.presentation.view_model.MediaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MediaActivity : AppCompatActivity() {
    //Log.i("MyTest", "MediaActivity.onCreate-4")
    companion object {
        private const val TIME_DEBOUNCE = 400L // время, через которое будет обновляться поле, показывающее, сколько времени от начала отрывка проиграно в формате
    }

    private lateinit var binding: ActivityMediaBinding

    private lateinit var timeTrack :TextView

    private var url: String? = ""

    /*private val viewModel by lazy {
        ViewModelProvider(this, MediaViewModel.getMediaViewModelfactory(url)
        )[MediaViewModel::class.java]
    }*/
    private val viewModel by viewModel<MediaViewModel>{parametersOf(url)}

    private val handlerMain = Handler(Looper.getMainLooper())

    private val timeTrackRunnable = object : Runnable {
        override fun run() {
            // обновляем время
            timeTrack.text = SimpleDateFormatMapper.map(viewModel.getCurrentPosition())

            handlerMain?.postDelayed(this, TIME_DEBOUNCE)
        }
    }
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)

        timeTrack = binding.time
        timeTrack.text = getString(R.string.time_00_00)

        val view = binding.root
        setContentView(view)

        // кнопка "Назад"
        val buttonBackMedia = binding.toolbar
        buttonBackMedia.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // получаем данные трека из Intent
        var item: Track? = getIntent().getParcelableExtra(TRACK)

        // раскладываем эти данные по соответствующим вьюшкам
        var ivTrackImage: ImageView = binding.trackImage
        var tvTrackName: TextView = binding.trackNameData
        var tvArtistName: TextView = binding.artistNameData
        var tvTrackTime: TextView = binding.trackTimeMillisData  // длительность
        var tvCollectionName: TextView = binding.collectionNameData
        var tvReleaseDate: TextView = binding.releaseDateData
        var tvPrimaryGenreName: TextView = binding.primaryGenreNameData
        var tvCountry: TextView = binding.countryData

        if (item != null) {
            Glide.with(this)
                .load(item.getCoverArtwork())
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .transform(RoundedCorners(8))
                .into(ivTrackImage)

            tvTrackName.text = item.trackName
            tvArtistName.text = item.artistName
            tvTrackTime.text = SimpleDateFormatMapper.map(item.trackTimeMillis)
            tvCollectionName.text = item.collectionName
            tvReleaseDate.text = item.releaseDate.substring(0, 4)
            tvPrimaryGenreName.text = item.primaryGenreName
            tvCountry.text = item.country

            // ссылка на отрывок
            url = item?.previewUrl

            // подписываемся на состояние MediaViewModel
            viewModel.getMediaPlayerState().observe(this) { state ->
                when(state) {
                    is MediaPlayerState.Playing -> {
                        showPlaying()
                    }
                    is MediaPlayerState.Paused -> {
                        showPaused()
                    }
                    is MediaPlayerState.Prepared -> {
                        showPrepared()
                    }
                }
            }
            // обновляем время
            timeTrack.text = SimpleDateFormatMapper.map(viewModel.getCurrentPosition())

            // кнопка "Play"/"Pause"
            val buttonPlayPause = binding.buttonMediaPlayPause

            buttonPlayPause.setOnClickListener {
                viewModel.playbackControl()
            }
        } else {
            Toast.makeText(
                this,
                R.string.my_message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /*private val timeTrackRunnable = object : Runnable {
           override fun run() {
               var timeTrack = findViewById<TextView>(R.id.time)
               // обновляем время
               //timeTrack.text = SimpleDateFormatMapper.map(mediaPlayer.currentPosition) //p1
               timeTrack.text = SimpleDateFormatMapper.map(mediaPlayerInteractor.getCurrentPosition()) //p1
               handlerMain?.postDelayed(this, TIME_DEBOUNCE)
           }
       }*/

    // подготовка плейера
    /*private fun preparePlayer() {
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
    }*/ //p1

    // запустить плейер
    /* private fun startPlayer() {
         // кнопка "Play"/"Pause"
         val buttonPlayPause = findViewById<ImageView>(R.id.button_media_play_pause)

         mediaPlayer.start()
         buttonPlayPause.setImageResource(R.drawable.button_media_pause)
         playerState = STATE_PLAYING

         handlerMain?.postDelayed(
             timeTrackRunnable,
             TIME_DEBOUNCE
         )  // ставим в очередь обновление таймера
     }*/ //p1

    // поставить плейер на паузу
    /*private fun pausePlayer() {
        // кнопка "Play"/"Pause"
        val buttonPlayPause = findViewById<ImageView>(R.id.button_media_play_pause)

        mediaPlayer.pause()
        buttonPlayPause.setImageResource(R.drawable.button_media_play)
        playerState = STATE_PAUSED

        handlerMain?.removeCallbacks(timeTrackRunnable) // удаляем из очереди все сообщения Runnable, чтобы таймер не обновлялся
    }*/ //p1

    // функция вызывается при нажатии на кнопку Play/Pause
    /* private fun playbackControl() {
         when (playerState) {
             STATE_PLAYING -> {
                 pausePlayer()
             }

             STATE_PREPARED, STATE_PAUSED -> {
                 startPlayer()
             }
         }
     }*/ //p1

    // Активити на паузу
    override fun onPause() {
        super.onPause()
            // viewModel.pausePlayer()
    }

    // Активити закрывается
    override fun onDestroy() {
        super.onDestroy()
       // viewModel.onDestroyMediaPlayer() - убрала, чтобы медиаплейер не перезапускался снова при повороте
    }

    fun showPlaying(){
        // запустить плейер        // кнопка "Play"/"Pause"
        val buttonPlayPause = binding.buttonMediaPlayPause
        buttonPlayPause.setImageResource(R.drawable.button_media_pause)

        handlerMain?.postDelayed(
            timeTrackRunnable,
            TIME_DEBOUNCE
        )  // ставим в очередь обновление таймера
    }

    fun showPaused(){
        // кнопка "Play"/"Pause"
        val buttonPlayPause = binding.buttonMediaPlayPause
        buttonPlayPause.setImageResource(R.drawable.button_media_play)

        handlerMain?.removeCallbacks(timeTrackRunnable) // удаляем из очереди все сообщения Runnable, чтобы таймер не обновлялся
    }
    fun showPrepared() {
        var timeTrack = binding.time
        timeTrack.text = getString(R.string.time_00_00)

        handlerMain?.removeCallbacks(timeTrackRunnable) // удаляем из очереди все сообщения Runnable, чтобы таймер не обновлялся

    }
}