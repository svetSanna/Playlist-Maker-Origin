package com.example.playlistmaker.ui.MediatekaFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.state.PlaylistsScreenState
import com.example.playlistmaker.presentation.state.SearchScreenState
import com.example.playlistmaker.presentation.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.AdapterAndViewHolder.PlaylistAdapter
import com.example.playlistmaker.ui.AdapterAndViewHolder.PlaylistViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment :Fragment(), PlaylistViewHolder.OnPlaylistClickListener  {
    companion object {
          fun newInstance() = PlaylistsFragment()
    }
    private var _binding: FragmentPlaylistsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // lдля получения списка плейлистов
    private var playlistsList: ArrayList<Playlist> = arrayListOf()
    // создаем адаптер для Playlist
    private val playlistAdapter = PlaylistAdapter()

    private val viewModel by viewModel<PlaylistsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val placeholderImage: ImageView = binding.errorImagePlaylist
       // val placeholderLayout: LinearLayout = binding.errorLayoutPlaylists

       // placeholderLayout.visibility = View.VISIBLE
        placeholderImage.setImageResource(R.drawable.nothing_found)

        playlistAdapter.onPlaylistClickListener = this

        viewModel.loadData()

        val rvItems: RecyclerView = binding.recyclerViewPlaylist
        rvItems.apply {
            adapter = playlistAdapter
            layoutManager =
                GridLayoutManager(requireContext(), /*Количество столбцов*/ 2) //ориентация по умолчанию — вертикальная
                // LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        playlistAdapter.items = playlistsList

        // подписываемся на состояние SearchViewModel
        viewModel.getScreenState().observe(viewLifecycleOwner) { state ->
            when(state) {
                is PlaylistsScreenState.Error -> {
                    showError()//state.message)
                }
                is PlaylistsScreenState.Content -> {
                    showContent(state.data)
                }
            }
        }

        /*val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = GridLayoutManager(this, /*Количество столбцов*/ 2) //ориентация по умолчанию — вертикальная
        recyclerView.adapter = NewsAdapter() */


        binding.buttonGoToNewPlaylist.setOnClickListener{
            // переход на экран аудиоплейера, передаем выбранный трек

            findNavController().navigate(R.id.action_mediatekaFragment_to_newPlayListFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPlaylistClick(item: Playlist) {
        Toast.makeText(requireContext(), "Кликнули", Toast.LENGTH_LONG).show()
    }

    private fun showError() {
        binding.placeholderPlaylist.visibility = View.VISIBLE
        binding.recyclerViewPlaylist.visibility = View.GONE

        playlistsList.clear()
        playlistAdapter.notifyDataSetChanged()
    }

    private fun showContent(data: List<Playlist>) { // отображение контента, полученного после запроса, т.е. состояние SearchScreenState = content
        binding.placeholderPlaylist.visibility = View.GONE
        binding.recyclerViewPlaylist.visibility = View.VISIBLE

        val rvItems: RecyclerView = binding.recyclerViewPlaylist
        rvItems.apply {
            adapter = playlistAdapter
            layoutManager = GridLayoutManager(requireContext(), /*Количество столбцов*/ 2) //ориентация по умолчанию — вертикальная

            //LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        playlistsList.clear()

        playlistsList.addAll(data)
        playlistAdapter.items = playlistsList
        playlistAdapter.notifyDataSetChanged()
    }
}