package com.example.playlistmaker.ui.PlaylistFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.App.Companion.getEndingMinute
import com.example.playlistmaker.App.Companion.getEndingTrack
import com.example.playlistmaker.presentation.view_model.PlaylistViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.ui.AdapterAndViewHolder.TrackAdapter
import com.example.playlistmaker.ui.AdapterAndViewHolder.TrackViewHolder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : Fragment(), TrackViewHolder.OnItemClickListener,
                        TrackViewHolder.OnLongClickListener{
    private var _binding: FragmentPlaylistBinding? = null
    private val binding
        get() = _binding!!

    //private lateinit var timeTrack : TextView

    //private var url: String? = ""
    var playlist: Playlist? = null

    private var trackList: MutableList<Track> = arrayListOf()

    // создаем адаптер для треков
    private val trackAdapter = TrackAdapter()

    private val viewModel by viewModel<PlaylistViewModel> { parametersOf(playlist) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // кнопка "Назад"
        val buttonBackMedia = binding.toolbar
        buttonBackMedia.setOnClickListener {
            findNavController().navigateUp()
        }

        // bottomSheet
        val bottomSheetContainer = binding.bottomSheetTracksInPlaylist
        // val overlay = binding.overlay
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
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

                /*  when (newState) {
                      BottomSheetBehavior.STATE_HIDDEN -> {
                          overlay.visibility = View.GONE
                      }
                      else -> {
                          overlay.visibility = View.VISIBLE
                      }
                  }*/
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Чтобы интенсивность затеменения менялась плавно в зависимости от положения Bottom Sheet,
                // можете добавить в метод onSlide() у этого же слушателя изменение значения alpha,
                // используя переменную slideOffset:

                //   overlay.alpha = (slideOffset + 1)/2

                // Однако вам потребуется доработать этот код самостоятельно, ведь необходимо учесть,
                // что значение альфы варьируется от 0f до 1f, тогда как slideOffset имеет диапазон от -1f до 1f.
            }
        })

        // получаем данные плейлиста
        val args: PlaylistFragmentArgs by navArgs()
        playlist = args.playlist

        // раскладываем эти данные по соответствующим вьюшкам
        var ivPlaylistImage: ImageView = binding.playlistImage
        var tvPlaylistName: TextView = binding.playlistName
        var tvPlaylistDefinition: TextView = binding.playlistDefinition
        var tvPlaylistTime: TextView = binding.playlistTime
        var tvPlaylistNumberOfTracks: TextView = binding.playlistNumberOfTracks

        if (playlist != null) {
            Glide.with(this)
                .load(playlist!!.path)
                .placeholder(R.drawable.place_holder)
//                .fitCenter()
                .into(ivPlaylistImage)

            tvPlaylistName.text = playlist!!.name
            if (playlist!!.definition.isNullOrBlank()) {
                tvPlaylistDefinition.visibility = View.GONE
            } else {
                tvPlaylistDefinition.visibility = View.VISIBLE
                tvPlaylistDefinition.text = playlist!!.definition
            }

            tvPlaylistNumberOfTracks.text =
                "0 треков"//playlist!!.count.toString() + " трек" + getEnding(playlist!!.count)
            tvPlaylistTime.text =
                "0 минут"//SimpleDateFormat("mm", Locale.getDefault()).format(sumTime(trackList))

            //      if (playlist != null) {
            trackAdapter.onItemClickListener = this
            trackAdapter.onLongClickListener = this
            viewModel.loadTracks(playlist!!.playlistId)

            viewModel.getTracksMutableData().observe(viewLifecycleOwner) { tracks ->
                if (tracks != null) {
                    val rvItems: RecyclerView = binding.recyclerviewTracksInPlaylist
                    rvItems.apply {
                        adapter = trackAdapter
                        layoutManager =
                            LinearLayoutManager(requireContext()) //ориентация по умолчанию — вертикальная
                    }
                    trackList = tracks as MutableList<Track>

                    trackAdapter.items = trackList
                    trackAdapter.notifyDataSetChanged()

                    tvPlaylistNumberOfTracks.text =
                        //playlist!!.count.toString() + getEndingTrack(playlist!!.count)
                        trackList.count().toString() + getEndingTrack(trackList.count())
                    val sum = sumTime(trackList)
                    tvPlaylistTime.text =
                        SimpleDateFormat("mm", Locale.getDefault()).format(sum) + getEndingMinute(sum.toInt())
                }
            }
        }
    }

    fun sumTime(trackList: List<Track>): Long {
        var time = 0L
        for (track in trackList) {
            time += track.trackTimeMillis
        }
        return time
    }

    override fun onItemClick(item: Track) {
        // переход на экран аудиоплейера, передаем выбранный трек
        val direction = PlaylistFragmentDirections.actionPlaylistFragmentToMediaFragment(item)
        findNavController().navigate(direction)
    }

    override fun onLongClick(item: Track): Boolean {
        // создаем диалог
        var deleteTrackDialog: MaterialAlertDialogBuilder
        deleteTrackDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.title_delete_track_dialog)) // Заголовок диалога
            .setMessage(getString(R.string.message_delete_track_dialog)) // Описание диалога
            .setNeutralButton(getString(R.string.cancel_dialog)) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }
            .setPositiveButton(getString(R.string.del_dialog)) { dialog, which -> // Добавляет кнопку «Завершить»
                // Действия, выполняемые при нажатии на кнопку «Да»
                viewModel.deleteTrackFromPlaylist(item, playlist!!.playlistId)
            }

        deleteTrackDialog.show()
        return true
    }
}