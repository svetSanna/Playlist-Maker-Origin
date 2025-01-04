package com.example.playlistmaker.ui.SearchFragment

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.state.SearchScreenState
import com.example.playlistmaker.presentation.view_model.SearchViewModel
import com.example.playlistmaker.ui.AdapterAndViewHolder.TrackAdapter
import com.example.playlistmaker.ui.AdapterAndViewHolder.TrackViewHolder
import com.example.playlistmaker.ui.MediatekaFragment.MediatekaFragmentDirections
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackViewHolder.OnItemClickListener {
    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

    private var editString: String = ""
    private var trackList: ArrayList<Track> = arrayListOf()

    // создаем адаптер для Track
    private val trackAdapter = TrackAdapter()

    // создаем адаптер для Track для истории поиска
    private var trackAdapterSearchHistory = TrackAdapter()

    private val viewModel by viewModel<SearchViewModel>()

    companion object {
        private const val CLICK_DEBOUNCE_DELAY =
            1000L      // для предотвращения нажатия два раза подряд на элемент списка
        private const val EDIT_STRING = "editString"
        private const val TRACK_LIST = "track_list"
        private const val TRACK_LIST_SEARCH_HISTORY = "track_list_search_history"
    }

    private var isClickAllowed = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackAdapterSearchHistory.onItemClickListener = this
        trackAdapter.onItemClickListener = this

        val inputEditText = binding.editSearchWindow

        viewModel.loadData(inputEditText.text.toString())

        // для поиска
        val rvItems1: RecyclerView = binding.rvItems
        rvItems1.apply {
            adapter = trackAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        trackAdapter.items = trackList

        // для истории поиска
        val rvItemsSearchHistory: RecyclerView = binding.rvSearchHistoryItems
        rvItemsSearchHistory.apply {
            adapter =
                trackAdapterSearchHistory
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        // подписываемся на состояние SearchViewModel
        viewModel.getScreenState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchScreenState.Loading -> {
                    progressBarVisibility(true)
                }

                is SearchScreenState.Error -> {
                    progressBarVisibility(false)
                    showError(state.message)
                }

                is SearchScreenState.Content -> {
                    progressBarVisibility(false)
                    showContent(state.data)
                }

                is SearchScreenState.ContentHistory -> {
                    progressBarVisibility(false)
                    showHistory(state.data)
                }
            }
        }

        // кнопка "Обновить"
        val buttonRefresh = binding.buttonRefresh
        buttonRefresh.visibility = View.GONE
        buttonRefresh.setOnClickListener {
            inputEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.placeholderLinearLayout.visibility = View.GONE
                binding.rvItems.visibility = View.GONE
                binding.searchHistoryLinearLayout.visibility = View.GONE
                Log.i(
                    "MyTest",
                    "SearchFragment.inputEditText.setOnEditorActionListener: вызывается searchRequest() "
                )
                searchRequest()
                true
            }
            false
        }

        // кнопка "очистить поле ввода"
        val clearButton = binding.buttonCloseClearCancel
        clearButton.setOnClickListener {
            inputEditText.setText("")

            val imm =
                requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(inputEditText.getWindowToken(), 0)

            trackAdapter.items.clear()
            trackAdapter.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //    TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)

                binding.searchHistoryLinearLayout.visibility =
                    if (inputEditText.hasFocus() && (s?.isEmpty() == true))
                        View.VISIBLE
                    else View.GONE
                if (trackAdapterSearchHistory.itemCount == 0)
                    binding.searchHistoryLinearLayout.visibility = View.GONE

                trackList.clear()
                trackAdapter.items = trackList
                trackAdapter.notifyDataSetChanged()

                binding.placeholderLinearLayout.visibility = View.GONE
                binding.rvItems.visibility = View.GONE

                Log.i(
                    "MyTest",
                    "SearchFragment.simpleTextWatcher.onTextChanged: вызывается viewModel.SearchDebounce() "
                )
                viewModel.searchDebounce(inputEditText.text.toString()) //searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                //  TODO("Not yet implemented")
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        // отображаем LinearLayout истории поиска, если фокус находится в inputEditText и inputEditText пуст
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            binding.searchHistoryLinearLayout.visibility =
                if (hasFocus && inputEditText.text.isEmpty())
                    View.VISIBLE
                else View.GONE
            if (trackAdapterSearchHistory.itemCount == 0)
                binding.searchHistoryLinearLayout.visibility = View.GONE
        }

        inputEditText.requestFocus()

        // кнопка "Очистить историю"
        binding.buttonCleanSearchHistory.setOnClickListener {

            viewModel.searchHistoryClean()
            trackAdapterSearchHistory.items.clear()
            trackAdapterSearchHistory.notifyDataSetChanged()

            binding.searchHistoryLinearLayout.visibility = View.GONE

            viewModel.writeToSharedPreferences()
        }
    }

    private fun showHistory(data: List<Track>) {
        trackAdapterSearchHistory.items = data.toMutableList()
        binding.rvItems.visibility = View.GONE
        if (trackAdapterSearchHistory.itemCount == 0) {
            binding.searchHistoryLinearLayout.visibility = View.GONE
        } else binding.searchHistoryLinearLayout.visibility = View.VISIBLE
        binding.placeholderLinearLayout.visibility = View.GONE
    }

    // Поисковый запрос
    private fun searchRequest() {
        val inputEditText = binding.editSearchWindow

        if (inputEditText.text.isNotEmpty()) {
            progressBarVisibility(true)// Показываем ProgressBar перед выполнением запроса

            viewModel.searchDebounce(inputEditText.text.toString())

            progressBarVisibility(false) // Прячем ProgressBar после выполнения запроса
        }
    }

    private fun showError(code: String) {
        // кнопка "Обновить"
        val buttonRefresh = binding.buttonRefresh
        buttonRefresh.visibility = View.VISIBLE
        showMessage(getString(R.string.something_went_wrong))
        binding.rvItems.visibility = View.GONE
        binding.searchHistoryLinearLayout.visibility = View.GONE
        binding.placeholderLinearLayout.visibility = View.VISIBLE
    }

    private fun showContent(data: List<Track>) { // отображение контента, полученного после запроса, т.е. состояние SearchScreenState = content
        // кнопка "Обновить"
        val buttonRefresh = binding.buttonRefresh
        buttonRefresh.visibility = View.GONE

        val rvItems1: RecyclerView = binding.rvItems
        rvItems1.apply {
            adapter = trackAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        trackList.clear()

        trackList.addAll(data)
        trackAdapter.items = trackList
        trackAdapter.notifyDataSetChanged()

        if (trackList.isEmpty()) {
            showMessage(getString(R.string.nothing_found))//, "")
            binding.rvItems.visibility = View.GONE
            binding.searchHistoryLinearLayout.visibility = View.GONE
            binding.placeholderLinearLayout.visibility = View.VISIBLE
        } else {
            showMessage("")//, "")
            binding.rvItems.visibility = View.VISIBLE
            binding.searchHistoryLinearLayout.visibility = View.GONE
            binding.placeholderLinearLayout.visibility = View.GONE
        }
    }

    private fun progressBarVisibility(isVisible: Boolean) {
        var progressBar = binding.progressBar

        if (isVisible) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    private fun showMessage(text: String) {//, additionalMessage: String) {

        val placeholderMessage: TextView = binding.placeholderMessage
        val placeholderImage: ImageView = binding.placeholderImage

        if (text.isNotEmpty()) {
            when (text) {
                getString(R.string.nothing_found) ->
                    placeholderImage.setImageResource(R.drawable.nothing_found)

                getString(R.string.something_went_wrong) ->
                    placeholderImage.setImageResource(R.drawable.something_went_wrong)
            }
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            placeholderMessage.text = text
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_STRING, editString)

        outState.putParcelableArrayList(TRACK_LIST, trackList as ArrayList<out Parcelable?>?)

        outState.putParcelableArrayList(
            TRACK_LIST_SEARCH_HISTORY,
            viewModel.getTrackListSearchHistory()
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        if (savedInstanceState != null) {
            editString = savedInstanceState!!.getString(EDIT_STRING, "")
            val inputEditText = binding.editSearchWindow
            inputEditText.setText(editString)

            trackList = savedInstanceState.getParcelableArrayList(TRACK_LIST)!!
            trackAdapter.items = trackList
            trackAdapter.notifyDataSetChanged()

            hidePlaceholder()

            viewModel.setTrackListSearchHistory(
                savedInstanceState.getParcelableArrayList(
                    TRACK_LIST_SEARCH_HISTORY
                )!!
            )
            trackAdapterSearchHistory.items = viewModel.getTrackListSearchHistory()
            trackAdapterSearchHistory.notifyDataSetChanged()
        }
    }

    //скрыть PlaceHolder
    private fun hidePlaceholder() {
        binding.rvItems.visibility = View.VISIBLE
        binding.placeholderLinearLayout.visibility = View.GONE
    }

    override fun onItemClick(item: Track) {
        if (clickDebounce()) { // если между нажатиями на элемент прошло не меньше 1 секунды

            viewModel.itemClick(item)

            trackAdapterSearchHistory.items = viewModel.getTrackListSearchHistory()
            trackAdapterSearchHistory.notifyDataSetChanged()

            viewModel.addItemToTrackListSearchHistory(item)
            trackAdapterSearchHistory.items = viewModel.getTrackListSearchHistory()
            trackAdapterSearchHistory.notifyDataSetChanged()

            viewModel.writeToSharedPreferences()

            // переход на экран аудиоплейера, передаем выбранный трек
            val direction =
                SearchFragmentDirections.actionSearchFragmentToMediaFragment(item) //.actionSearchFragmentToMediaActivity(item)
            findNavController().navigate(direction)
        }
    }

    // Предотвращение двойного нажатия на элемент списка
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            //  viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }
}