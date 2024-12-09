package com.example.playlistmaker.ui.NewPlayListFragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.presentation.view_model.NewPlayListViewModel

class NewPlayListFragment : Fragment() {
    private var _binding: FragmentNewPlayListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

  /*  companion object {
        fun newInstance() = NewPlayListFragment()
    }*/

    private val viewModel: NewPlayListViewModel by viewModels()

        /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleEditText = binding.titleEdittext

        //viewModel.loadData(inputEditText.text.toString())

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //    TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrBlank()) {
                    binding.buttonNewPlaylist.isEnabled = false
                }
                else {
                    binding.buttonNewPlaylist.isEnabled = true
                }

            }

            override fun afterTextChanged(s: Editable?) {
                //  TODO("Not yet implemented")
            }
        }
        titleEditText.addTextChangedListener(simpleTextWatcher)
    }
}