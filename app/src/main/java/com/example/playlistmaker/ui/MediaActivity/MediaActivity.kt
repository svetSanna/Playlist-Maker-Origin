package com.example.playlistmaker.ui.MediaActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.mapper.SimpleDateFormatMapper
import com.example.playlistmaker.presentation.state.LikeButtonState
import com.example.playlistmaker.presentation.state.MediaPlayerState
import com.example.playlistmaker.presentation.view_model.MediaViewModel
import com.example.playlistmaker.ui.AdapterAndViewHolder.ChosePlaylistViewHolder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MediaActivity : AppCompatActivity(), ChosePlaylistViewHolder.OnChosePlaylistClickListener {
    //Log.i("MyTest", "MediaActivity.onCreate-4")

    private lateinit var binding: ActivityMediaBinding
    private lateinit var timeTrack :TextView

    private var url: String? = ""
    var item: Track? = null

    private val viewModel by viewModel<MediaViewModel>{parametersOf(item)}//() //{parametersOf(url)}

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)

        timeTrack = binding.time

        val view = binding.root
        setContentView(view)

        // кнопка "Назад"
        val buttonBackMedia = binding.toolbar
        buttonBackMedia.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // bottomSheet
        val bottomSheetContainer = binding.standardBottomSheet
        val overlay = binding.overlay

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        /*
        BottomSheetBehavior имеет несколько состояний-констант, как было показано на видео выше:
        STATE_COLLAPSED — дефолтное положение BottomSheet, при котором BottomSheet находится в нераскрытом виде.
        STATE_EXPANDED — раскрытое положение BottomSheet, соответствующее его максимально возможной высоте (match_parent).
        STATE_HIDDEN — полностью свёрнутое положение BottomSheet, в которое его можно привести перетаскиванием/взмахом вниз из состояния STATE_COLLAPSED.
        STATE_DRAGGING — состояние BottomSheet, при котором пользователь перетаскивает его вверх или вниз.
        STATE_SETTLING — BottomSheet устанавливается на определённую высоту после жеста перетаскивания (dragging)/взмахивания.
        */

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Чтобы интенсивность затеменения менялась плавно в зависимости от положения Bottom Sheet,
                // можете добавить в метод onSlide() у этого же слушателя изменение значения alpha,
                // используя переменную slideOffset:

              //  overlay.alpha = slideOffset

                // Однако вам потребуется доработать этот код самостоятельно, ведь необходимо учесть,
                // что значение альфы варьируется от 0f до 1f, тогда как slideOffset имеет диапазон от -1f до 1f.
            }
        })

        // получаем данные трека из Intent
        val args: MediaActivityArgs by navArgs()
        //var item: Track? = args.item
        item = args.item

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
                .load(item!!.getCoverArtwork())
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .transform(RoundedCorners(8))
                .into(ivTrackImage)

            tvTrackName.text = item!!.trackName
            tvArtistName.text = item!!.artistName
            tvTrackTime.text = SimpleDateFormatMapper.map(item!!.trackTimeMillis)
            tvCollectionName.text = item!!.collectionName
            tvReleaseDate.text = item!!.releaseDate.substring(0, 4)
            tvPrimaryGenreName.text = item!!.primaryGenreName
            tvCountry.text = item!!.country

            // ссылка на отрывок
            url = item?.previewUrl

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

            // кнопка "Play"/"Pause"
            val buttonPlayPause = binding.buttonMediaPlayPause
            buttonPlayPause.setOnClickListener {
                viewModel.playbackControl()
            }

            viewModel.getLikeButtonState().observe(this) { state ->
                when(state) {
                    is LikeButtonState.Liked -> {
                        val buttonLike = binding.buttonMediaLike
                        buttonLike.setImageResource(R.drawable.button_media_like_chosen)
                    }
                    is LikeButtonState.Unliked -> {
                        val buttonLike = binding.buttonMediaLike
                        buttonLike.setImageResource(R.drawable.button_media_like)
                    }
                }
            }

            // кнопка "добавить в избранное/ удалить из избранного"
            val likeButton = binding.buttonMediaLike
            likeButton.setOnClickListener {
                viewModel.onFavoriteClicked()//(item)
            }

            // кнопка добавить в плейлист
            val addToPlaylistButton = binding.buttonMediaAdd
            addToPlaylistButton.setOnClickListener {
               // viewModel.onAddToPlaylistClicked()//(item)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            // кнопка "новый плейлист"
            val newPlaylistButton = binding.buttonAddToPlaylist
            newPlaylistButton.setOnClickListener {
                // переход на экран создания нового плейлиста

               // findNavController(R.id.rootFragmentContainerView).navigate(R.id.action_mediaActivity_to_newPlayListFragment)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("playlistmaker://newPlaylist"))
                startActivity(intent)

               // val navHostFragment = supportFragmentManager.findFragmentById(R.id.newPlayListFragment) as NavHostFragment
               // navController = navHostFragment.navController
                //findNavController(R.id.rootFragmentContainerView).navigate(R.id.action_mediatekaFragment_to_newPlayListFragment)
               // Navigation.findNavController(this, R.id.my_nav_host_fragment).navigate(R.id.action_navigationFragment_to_blankFragment)

                //Navigation.findNavController(this, R.id.newPlayListFragment)

               // findNavController(RootActivity, R.id.rootFragmentContainerView).navigate(R.id.newPlayListFragment)
               // findNavController(R.id.newPlayListFragment).navigate(R.id.action_mediatekaFragment_to_newPlayListFragment)

            //val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
            //val navController = navHostFragment.navController


                /*val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.newPlayListFragment)*/


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
        //Log.i("MyTest", "MediaActivity.onDestroy() ")
       // viewModel.onDestroyMediaPlayer() - убрала, чтобы медиаплейер не перезапускался снова при повороте
    }

    override fun onStop() {
        super.onStop()
        //Log.i("MyTest", "MediaActivity.onStop() ")
        if (!isChangingConfigurations){
            viewModel.onPause()
        }
    }

    fun showPlaying(){
        // запустить плейер        // кнопка "Play"/"Pause"
       // Log.i("MyTest", "MediaActivity.showPlaying() ")
        val buttonPlayPause = binding.buttonMediaPlayPause
        buttonPlayPause.setImageResource(R.drawable.button_media_pause)
        // обновляем время
        timeTrack.text = SimpleDateFormatMapper.map(viewModel.getCurrentPosition())
    }

    fun showPaused(){
        // кнопка "Play"/"Pause"
     //   Log.i("MyTest", "MediaActivity.showPaused() ")
        val buttonPlayPause = binding.buttonMediaPlayPause
        buttonPlayPause.setImageResource(R.drawable.button_media_play)
        // обновляем время
        timeTrack.text = SimpleDateFormatMapper.map(viewModel.getCurrentPosition())
    }
    fun showPrepared() {
       // Log.i("MyTest", "MediaActivity.onPrepared() ")
        var timeTrack = binding.time
        timeTrack.text = getString(R.string.time_00_00)
        // кнопка "Play"/"Pause"
        val buttonPlayPause = binding.buttonMediaPlayPause
        buttonPlayPause.setImageResource(R.drawable.button_media_play)
   }

    override fun onChosePlaylistClick(item: Playlist) {
            Toast.makeText(this, "Кликнули", Toast.LENGTH_LONG).show()
    }
}