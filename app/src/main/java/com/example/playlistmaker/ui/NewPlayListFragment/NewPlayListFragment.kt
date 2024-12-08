package com.example.playlistmaker.ui.NewPlayListFragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.view_model.NewPlayListViewModel

class NewPlayListFragment : Fragment() {

    companion object {
        fun newInstance() = NewPlayListFragment()
    }

    private val viewModel: NewPlayListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_new_play_list, container, false)
    }
}