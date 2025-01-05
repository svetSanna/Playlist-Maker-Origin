package com.example.playlistmaker.ui.EditPlaylistFragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import com.example.playlistmaker.domain.entity.Playlist
import com.example.playlistmaker.presentation.view_model.EditPlaylistViewModel
import com.example.playlistmaker.ui.NewPlaylistFragment.NewPlaylistFragment
import com.example.playlistmaker.ui.PlaylistFragment.PlaylistFragmentArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class EditPlaylistFragment(/*val playlist: Playlist*/) : NewPlaylistFragment() {
    private var _binding: FragmentNewPlayListBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

    override var imageUri: Uri? = null
    var playlist: Playlist? = null

    private val viewModel by viewModel<EditPlaylistViewModel> { parametersOf(playlist) } /// override

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

        // кнопка "Назад"
        val buttonBackMedia = binding.toolbarPlaylist
        buttonBackMedia.setOnClickListener {
            findNavController().navigateUp()
        }

        // получаем данные плейлиста
        val args: EditPlaylistFragmentArgs by navArgs()
        playlist = args.playlist

        binding.toolbarPlaylistTextview.setText(R.string.edit_playlist)
        binding.buttonCreateNewPlaylist.setText(R.string.save)
        binding.titleEdittext.setText(playlist!!.name)
        binding.definitionEdittext.setText(playlist!!.definition)
        binding.imagePlaylist.setImageURI(playlist!!.path?.toUri())

        binding.buttonCreateNewPlaylist.isEnabled = true

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //    TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    binding.buttonCreateNewPlaylist.isEnabled = false
                } else {
                    binding.buttonCreateNewPlaylist.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //  TODO("Not yet implemented")
            }
        }
        binding.titleEdittext.addTextChangedListener(simpleTextWatcher)

        // создаём событие с результатом и передаём в него PickVisualMedia()
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->//.PickVisualMedia()) { uri ->
                // Callback вызовется, когда пользователь выберет картинку
                if (uri != null) {
                    binding.imagePlaylist.setImageURI(uri)
                    imageUri = uri
                } else {
                    Log.d("PhotoPicker", "Ничего не выбрано")
                }
            }

        //по нажатию на кнопку imagePlaylist запускаем photo picker
        binding.imagePlaylist.setOnClickListener {
            // Вызываем метод launch и передаём параметр, чтобы предлагались только картинки
            pickMedia.launch(arrayOf("image/*"))
        }

        // кнопка "Сохранить"
        binding.buttonCreateNewPlaylist.setOnClickListener {
            savePlayList(playlist!!.playlistId)
        }
    }

    private fun savePlayList(playlistId: Int) {
        //var path: String? = null
        if (imageUri != null) {
            //path = saveImageToPrivateStorage(imageUri!!)
            playlist?.path = saveImageToPrivateStorage(imageUri!!) //path
        }
        //Log.d("PhotoPicker", "Выбранный URI: $uri")
        val title = binding.titleEdittext.text.toString()
        val definition = binding.definitionEdittext.text.toString()

        viewModel.savePlaylist(playlistId, playlist?.path, title, definition)

        playlist?.name = title
        playlist?.definition = definition
        //playlist?.path = path

        findNavController().navigateUp()
    }
}