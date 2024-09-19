package com.example.playlistmaker.ui.MediaActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.TRACK
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.mapper.SimpleDateFormatMapper
import com.example.playlistmaker.presentation.view_model.MediaViewModel


class MediaActivity : AppCompatActivity() {
    //private var mediaPlayer = MediaPlayer()

    //private var getMediaPlayerUseCase = Creator.provideMediaPlayerInteractor() //p1
    //private var mediaPlayer = getMediaPlayerUseCase() //р1


    private lateinit var binding: ActivityMediaBinding

    //private lateinit var mediaPlayerInteractor: MediaPlayerInteractor//p1
    /*private val mediaPlayerInteractor: MediaPlayerInteractor by lazy{
        Creator.provideMediaPlayerInteractor(this)
    }*/

    /* companion object {
            private const val STATE_DEFAULT = 0 // освобожден
            private const val STATE_PREPARED = 1 // подготовлен
            private const val STATE_PLAYING = 2 // воспроизводится
            private const val STATE_PAUSED = 3 // пауза
            private const val TIME_DEBOUNCE =
                400L // время, через которое будет обновляться поле, показывающее, сколько времени от начала отрывка проиграно в формате
        }

        private var playerState = STATE_DEFAULT // cостояние плейера
        */ //p1

    private val viewModel by lazy {
        ViewModelProvider(this, MediaViewModel.getMediaViewModelfactory()
        )[MediaViewModel::class.java]
       // ViewModelProvider(this)[MediaViewModel::class.java]
    }

    private var url: String? = ""

    //private val handlerMain = Handler(Looper.getMainLooper()) //p1

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        // кнопка "Назад"
        val buttonBackMedia = binding.toolbar
        buttonBackMedia.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // кнопка "Play"/"Pause"
        val buttonPlayPause = binding.buttonMediaPlayPause //findViewById<ImageView>(R.id.button_media_play_pause)

        // получаем данные трека из Intent
        var item: Track? = getIntent().getParcelableExtra(TRACK)

        // раскладываем эти данные по соответствующим вьюшкам
        var ivTrackImage: ImageView = binding.trackImage //findViewById(R.id.track_image)
        var tvTrackName: TextView = binding.trackNameData //findViewById(R.id.track_name_data)
        var tvArtistName: TextView = binding.artistNameData //findViewById(R.id.artist_name_data)
        var tvTrackTime: TextView = binding.trackTimeMillisData //findViewById(R.id.track_time_millis_data) // длительность
        var tvCollectionName: TextView = binding.collectionNameData //findViewById(R.id.collection_name_data)
        var tvReleaseDate: TextView = binding.releaseDateData //findViewById(R.id.release_date_data)
        var tvPrimaryGenreName: TextView = binding.primaryGenreNameData //findViewById(R.id.primary_genre_name_data)
        var tvCountry: TextView = binding.countryData //findViewById(R.id.country_data)

        if (item != null) {
            Glide.with(this)
                .load(item.getCoverArtwork())
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .transform(RoundedCorners(8))//(R.dimen.corner_radius_8))
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
           // mediaPlayerInteractor = Creator.provideMediaPlayerInteractor(this)

            // подготовка плейера
            // кнопка "Play"/"Pause"
            val buttonPlayPause = binding.buttonMediaPlayPause
            // подготавливаем плейер
            // mediaPlayerInteractor.preparePlayer(url)
            viewModel.preparePlayer(url)

            buttonPlayPause.setOnClickListener {
                //mediaPlayerInteractor.playbackControl()
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
        //mediaPlayerInteractor.pausePlayer()
        viewModel.pausePlayer()
    }

    // Активити закрывается
    override fun onDestroy() {
        super.onDestroy()
        //mediaPlayerInteractor.onDestroy()
        viewModel.onDestroymediaPlayer()
    }
}