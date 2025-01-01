package com.example.playlistmaker.ui.NewPlaylistFragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import com.example.playlistmaker.presentation.view_model.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


open class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlayListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

    lateinit var confirmDialog: MaterialAlertDialogBuilder

    open var imageUri: Uri? = null

    private val viewModel by viewModel<NewPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     //   if(_binding == null)
     //       _binding = FragmentNewPlayListBinding.inflate(inflater, container, false)


        // создаем диалог
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.title_dialog)) // Заголовок диалога
            .setMessage(getString(R.string.message_dialog)) // Описание диалога
            .setNeutralButton(getString(R.string.cancel_dialog)) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }
            .setPositiveButton(getString(R.string.complete_dialog)) { dialog, which -> // Добавляет кнопку «Завершить»
                // Действия, выполняемые при нажатии на кнопку «Да»
                findNavController().navigateUp()
            }

        // кнопка "Назад"
        val buttonBackMedia = binding.toolbarPlaylist
        buttonBackMedia.setOnClickListener {
            onBack()
        }

        //val titleEditText = binding.titleEdittext
        binding.toolbarPlaylistTextview.setText(R.string.new_playlist)

        //viewModel.loadData(inputEditText.text.toString())

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //    TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrBlank()) {
                    binding.buttonCreateNewPlaylist.isEnabled = false
                }
                else {
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
            registerForActivityResult(ActivityResultContracts.OpenDocument()){ uri->//.PickVisualMedia()) { uri ->
            // Callback вызовется, когда пользователь выберет картинку
            if (uri != null) {
                binding.imagePlaylist.setImageURI(uri)
                imageUri = uri
                //saveImageToPrivateStorage(uri)
                //Log.d("PhotoPicker", "Выбранный URI: $uri")
            } else {
                Log.d("PhotoPicker", "Ничего не выбрано")
            }
        }

        //по нажатию на кнопку imagePlaylist запускаем photo picker
        binding.imagePlaylist.setOnClickListener{
            // Вызываем метод launch и передаём параметр, чтобы предлагались только картинки
            pickMedia.launch(arrayOf("image/*"))//(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // добавление слушателя для обработки нажатия на кнопку Back
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        })

        // кнопка "Создать"
        binding.buttonCreateNewPlaylist.setOnClickListener{
            createPlayList()
        }
    }
    protected fun saveImageToPrivateStorage(uri: Uri) : String {
        val contentResolver = requireActivity().applicationContext.contentResolver

        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), getString(R.string.directory))
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }

        val currentDate: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, getString(R.string.filename) + "_" + currentDate + getString(R.string.extention)) //Calendar.getInstance().time    .toString())

        // передаём необходимый флаг на запись
        val takeFlags: Int = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, takeFlags)

        // создаём входящий поток байтов из выбранной картинки
        val inputStream = contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.path
    }
    private fun onBack() {
        // Получаем идентификатор ресурса drawable, который сейчас установлен для ImageView
        val currentDrawable = binding.imagePlaylist.drawable
        if (!binding.titleEdittext.text.isNullOrBlank() ||
            !binding.definitionEdittext.text.isNullOrBlank() ||
            (currentDrawable != null))
            confirmDialog.show()
        else findNavController().navigateUp()
    }
    private fun createPlayList(){
        var path:String? = null
        if(imageUri != null)
            path = saveImageToPrivateStorage(imageUri!!)
        //Log.d("PhotoPicker", "Выбранный URI: $uri")
        val title = binding.titleEdittext.text.toString()
        val definition = binding.definitionEdittext.text.toString()

        viewModel.createPlaylist(path, title, definition)

        Toast.makeText(requireContext(), "Плейлист "+ title + " создан", Toast.LENGTH_LONG).show()

        findNavController().navigateUp()
    }
}

// пытаемся загрузить фотографию из хранилища
/* val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
 val file = File(filePath, "first_cover.jpg")
 binding.imagePlaylist.setImageURI(file.toUri())
 */