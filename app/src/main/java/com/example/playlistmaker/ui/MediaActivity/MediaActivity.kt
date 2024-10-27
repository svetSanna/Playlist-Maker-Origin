package com.example.playlistmaker.ui.MediaActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
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
        const val ARGS_TRACK = "track"
        fun createArgs(track: Track): Bundle {
            return bundleOf(ARGS_TRACK to track)
        }
    }

    private lateinit var binding: ActivityMediaBinding

    private lateinit var timeTrack :TextView

    private var url: String? = ""

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

            //val navHostFragment = supportFragmentManager.findFragmentById(R.id.searchFragment) as NavHostFragment
            //val navController = navHostFragment.navController
            //navController.navigateUp()

            //findNavController(R.id.searchFragment).navigateUp()
        }

        // получаем данные трека из Intent
        //var item: Track? = getIntent().getParcelableExtra(TRACK)
        val args: MediaActivityArgs by navArgs()
        var item: Track? = args.item

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
        }
    }

    // Активити на паузу
    override fun onPause() {
        super.onPause()
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