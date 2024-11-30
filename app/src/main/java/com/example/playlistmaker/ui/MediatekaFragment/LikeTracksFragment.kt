package com.example.playlistmaker.ui.MediatekaFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLikeTracksBinding
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.state.LikeTracksScreenState
import com.example.playlistmaker.presentation.view_model.LikeTracksViewModel
import com.example.playlistmaker.ui.AdapterAndViewHolder.TrackAdapter
import com.example.playlistmaker.ui.AdapterAndViewHolder.TrackViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class LikeTracksFragment : Fragment(), TrackViewHolder.OnItemClickListener {
    companion object {
        fun newInstance() = LikeTracksFragment()
    }
    private var _binding: FragmentLikeTracksBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModel<LikeTracksViewModel>()

    private var trackList: ArrayList<Track> = arrayListOf()

    // создаем адаптер для Track
    private val trackAdapter = TrackAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLikeTracksBinding.inflate(inflater, container, false)
        return binding.root
   }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackAdapter.onItemClickListener = this

        // для поиска
        val rvItems: RecyclerView = binding.rvLikeItems
        rvItems.apply {
            adapter = trackAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        trackAdapter.items = trackList

        // подписываемся на состояние SearchViewModel
        viewModel.getScreenState().observe(viewLifecycleOwner) { state ->
            when(state) {
                is LikeTracksScreenState.Content -> {
                    showContent(state.data)
                }
                is LikeTracksScreenState.Error -> {
                    showError(state.message)
                }
            }
        }
    }
    private fun showError(code: String) {
        trackList.clear()
        trackAdapter.notifyDataSetChanged()

        val placeholderImage: ImageView = binding.errorImageLikeTracks
        val placeholderLayout: LinearLayout = binding.errorLayoutLikeTracks

        placeholderLayout.visibility = View.VISIBLE
        placeholderImage.setImageResource(R.drawable.nothing_found)

        var recyclerView: RecyclerView = binding.rvLikeItems
        recyclerView.visibility = View.GONE
    }

    private fun showContent(data: List<Track>) {
        val recyclerView: RecyclerView = binding.rvLikeItems
        recyclerView.apply {
            adapter = trackAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        trackList.clear()

        trackList.addAll(data)
        trackAdapter.items = trackList
        trackAdapter.notifyDataSetChanged()

        val placeholderLayout: LinearLayout = binding.errorLayoutLikeTracks
        placeholderLayout.visibility = View.GONE

        recyclerView.visibility = View.VISIBLE
    }

    override fun onItemClick(item: Track) {
       // if (clickDebounce()) { // если между нажатиями на элемент прошло не меньше 1 секунды
            // переход на экран аудиоплейера, передаем выбранный трек
            val direction: NavDirections = MediatekaFragmentDirections.actionMediatekaFragmentToMediaActivity(item)
            findNavController().navigate(direction)
        //}
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }


    //скрыть PlaceHolder
   /* private fun hidePlaceholder() {
        binding.rvItems.visibility = View.VISIBLE
        binding.placeholderLinearLayout.visibility = View.GONE
    }

    override fun onItemClick(item: Track) {
        if (clickDebounce()) { // если между нажатиями на элемент прошло не меньше 1 секунды

            viewModel.itemClick(item)

            trackAdapterSearchHistory.items = viewModel.getTrackListSearchHistory()
            trackAdapterSearchHistory.notifyDataSetChanged()

            viewModel.addItemToTrackListSearchHistory(item)
            trackAdapterSearchHistory.items = viewModel.getTrackListSearchHistory()
            trackAdapterSearchHistory.notifyDataSetChanged()

            viewModel.writeToSharedPreferences()

            // переход на экран аудиоплейера, передаем выбранный трек
            val direction = SearchFragmentDirections.actionSearchFragmentToMediaActivity(item)
            findNavController().navigate(direction)
        }
    }

    // Предотвращение двойного нажатия на элемент списка
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(SearchFragment.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}