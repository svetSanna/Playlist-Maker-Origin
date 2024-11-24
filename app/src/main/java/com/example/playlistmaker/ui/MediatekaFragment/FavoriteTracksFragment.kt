package com.example.playlistmaker.ui.MediatekaFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.databinding.FragmentSearchBinding

class FavoriteTracksFragment : Fragment() {
    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
    private var _binding: FragmentFavoriteTracksBinding? = null
    //private lateinit var binding: FragmentFavoriteTracksBinding
    private val binding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
   }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val placeholderImage: ImageView = binding.errorImageFavouriteTracks
        val placeholderLayout: LinearLayout = binding.errorLayoutFavouriteTracks

        placeholderLayout.visibility = View.VISIBLE

        placeholderImage.setImageResource(R.drawable.nothing_found)

    }
}