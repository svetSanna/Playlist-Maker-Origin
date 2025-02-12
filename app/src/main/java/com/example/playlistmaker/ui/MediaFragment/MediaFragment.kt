package com.example.playlistmaker.ui.MediaFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.mapper.SimpleDateFormatMapper
import com.example.playlistmaker.presentation.state.LikeButtonState
import com.example.playlistmaker.presentation.state.MediaPlayerState
import com.example.playlistmaker.presentation.view_model.MediaViewModel
import com.example.playlistmaker.ui.AdapterAndViewHolder.ChosePlaylistAdapter
import com.example.playlistmaker.ui.AdapterAndViewHolder.ChosePlaylistViewHolder
//import com.example.playlistmaker.ui.MediaActivity.MediaActivityArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediaFragment : Fragment(), ChosePlaylistViewHolder.OnChosePlaylistClickListener {
    private var _binding: FragmentMediaBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

    private lateinit var timeTrack: TextView

    var item: Track? = null

    private var playlistsList: MutableList<Playlist> = arrayListOf()

    // создаем адаптер для playlist
    private val playlistAdapter = ChosePlaylistAdapter()

    private val viewModel by viewModel<MediaViewModel> { parametersOf(item) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeTrack = binding.time
        // кнопка "Назад"
        val buttonBackMedia = binding.toolbar
        buttonBackMedia.setOnClickListener {
            findNavController().navigateUp()//onBackPressedDispatcher.onBackPressed()
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

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
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

                overlay.alpha = (slideOffset + 1) / 2

                // Однако вам потребуется доработать этот код самостоятельно, ведь необходимо учесть,
                // что значение альфы варьируется от 0f до 1f, тогда как slideOffset имеет диапазон от -1f до 1f.
            }
        })

        // получаем данные трека из Intent
        val args: MediaFragmentArgs by navArgs()
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

            viewModel.getMediaPlayerState().observe(viewLifecycleOwner) { state ->
                when (state) {
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

            viewModel.getLikeButtonState().observe(viewLifecycleOwner) { state ->//(this) { state ->
                when (state) {
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
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            // кнопка "новый плейлист"
            val newPlaylistButton = binding.buttonAddToPlaylist
            newPlaylistButton.setOnClickListener {
                // переход на экран создания нового плейлиста
                findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment)
            }

            playlistAdapter.onChosePlaylistClickListener = this
            viewModel.loadPlaylists()

            viewModel.getPlaylistsMutableData().observe(viewLifecycleOwner) { list ->
                if (list != null) {
                    val rvItems: RecyclerView = binding.recyclerviewChosePlaylist
                    rvItems.apply {
                        adapter = playlistAdapter
                        layoutManager =
                            LinearLayoutManager(requireContext()) //ориентация по умолчанию — вертикальная
                    }
                    playlistsList = list as MutableList<Playlist>

                    playlistAdapter.items = playlistsList
                    playlistAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun showPlaying() {
        // запустить плейер        // кнопка "Play"/"Pause"
        // Log.i("MyTest", "MediaActivity.showPlaying() ")
        val buttonPlayPause = binding.buttonMediaPlayPause
        buttonPlayPause.setImageResource(R.drawable.button_media_pause)
        // обновляем время
        timeTrack.text = SimpleDateFormatMapper.map(viewModel.getCurrentPosition())
    }

    fun showPaused() {
        // кнопка "Play"/"Pause"
        //   Log.i("MyTest", "MediaActivity.showPaused() ")
        val buttonPlayPause = binding.buttonMediaPlayPause
        buttonPlayPause.setImageResource(R.drawable.button_media_play)
        // обновляем время
        timeTrack.text = SimpleDateFormatMapper.map(viewModel.getCurrentPosition())
    }

    fun showPrepared() {
        var timeTrack = binding.time
        timeTrack.text = getString(R.string.time_00_00)
        // кнопка "Play"/"Pause"
        val buttonPlayPause = binding.buttonMediaPlayPause
        buttonPlayPause.setImageResource(R.drawable.button_media_play)
    }

    override fun onChosePlaylistClick(playlist: Playlist) {
        if (playlist.trackIdList != null) {
            val idList = playlist.trackIdList!!.split(',')
            if (idList.contains(item!!.trackId.toString())) {
                Toast.makeText(
                    requireContext(),
                    "Трек уже добавлен в плейлист " + playlist.name,
                    Toast.LENGTH_LONG
                ).show()
                //val bottomSheetContainer = binding.standardBottomSheet
                //val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
                //bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                return
            }
        }
        // если трек с идентификатором, как у item, не нашелся ни в одном плейлисте
        viewModel.addTrackToPlaylist(item!!, playlist.playlistId)

        val bottomSheetContainer = binding.standardBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        Toast.makeText(requireContext(), "Добавлено в плейлист " + playlist.name, Toast.LENGTH_LONG)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }
}