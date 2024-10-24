package com.example.playlistmaker.ui.MediatekaFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding

class PlaylistsFragment :Fragment() {
    companion object {
          fun newInstance() = PlaylistsFragment()
    }
    private var _binding: FragmentPlaylistsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

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
        val placeholderLayout: LinearLayout = binding.errorLayoutPlaylists

        placeholderLayout.visibility = View.VISIBLE

        placeholderImage.setImageResource(R.drawable.nothing_found)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}