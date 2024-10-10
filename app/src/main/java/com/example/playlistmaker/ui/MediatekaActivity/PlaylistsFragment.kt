package com.example.playlistmaker.ui.MediatekaActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.presentation.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment :Fragment() {
    companion object {
          fun newInstance() = PlaylistsFragment()
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

        val placeholderImage: ImageView = binding.errorImagePlaylist
        val placeholderLayout: LinearLayout = binding.errorLayoutPlaylists

        placeholderLayout.visibility = View.VISIBLE

        placeholderImage.setImageResource(R.drawable.nothing_found)
    }
}