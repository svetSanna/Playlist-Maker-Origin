package com.example.playlistmaker.ui.MediatekaActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.presentation.view_model.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteTracksFragment : Fragment() {
    companion object {
        private const val NUMBER = "number"

        fun newInstance(number: Int) = FavoriteTracksFragment().apply {
            arguments = Bundle().apply {
                putInt(NUMBER, number)
            }
        }
    }
    private lateinit var binding: FragmentFavoriteTracksBinding

    private val viewModel by viewModel<FavoriteTracksViewModel>{
        parametersOf(requireArguments().getString(NUMBER))
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeFavoriteTracks().observe(viewLifecycleOwner) {
            showNumber(it)
        }
    }
    private fun showNumber(num: Int) {
        binding.number1.text = requireArguments().getInt(NUMBER).toString()
    }
}