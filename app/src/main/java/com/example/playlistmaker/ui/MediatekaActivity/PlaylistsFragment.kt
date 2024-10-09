package com.example.playlistmaker.ui.MediatekaActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.presentation.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment :Fragment() {
    companion object {
        private const val NUMBER_PLAYLIST = "number_playlist"
          fun newInstance(number: Int) = PlaylistsFragment().apply {
            arguments = Bundle().apply {
                putInt(PlaylistsFragment.NUMBER_PLAYLIST, number)
            }
        }
    }
    private lateinit var binding: FragmentPlaylistsBinding

    private val viewModel by viewModel<PlaylistsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observePlaylists().observe(viewLifecycleOwner) {
            showNumber(it)
        }
    }

    private fun showNumber(num: Int) {
        binding.number2.text = requireArguments().getInt(PlaylistsFragment.NUMBER_PLAYLIST).toString()
    }
}