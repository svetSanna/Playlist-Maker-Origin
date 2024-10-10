package com.example.playlistmaker.ui.SearchActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.R.string
import com.example.playlistmaker.TRACK
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.state.SearchScreenState
import com.example.playlistmaker.presentation.view_model.SearchViewModel
import com.example.playlistmaker.ui.MediaActivity.MediaActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), TrackViewHolder.OnItemClickListener {
    private var editString: String = ""
    private var trackList: ArrayList<Track> = arrayListOf()

    private lateinit var binding: ActivitySearchBinding

    // создаем адаптер для Track
    private val trackAdapter = TrackAdapter()

    // создаем адаптер для Track для истории поиска
    private var trackAdapterSearchHistory = TrackAdapter()

    private val viewModel by viewModel<SearchViewModel>()

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY =
            2000L     // для поиска при задержке ввода на 2 секунды
        private const val CLICK_DEBOUNCE_DELAY =
            1000L      // для предотвращения нажатия два раза подряд на элемент списка
        private const val EDIT_STRING = "editString"
        private const val TRACK_LIST = "track_list"
        private const val SEARCH_HISTORY_VISIBILITY = "search_history_visibility"
        private const val TRACK_LIST_SEARCH_HISTORY = "track_list_search_history"
    }

    // Создаём Handler, привязанный к ГЛАВНОМУ потоку
    private val searchRunnable = Runnable { SearchRequest() }

    private var mainHandler: Handler? = null

    private var isClickAllowed = true

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        // LinearLayout истории поиска
        val linearLayoutSearchHistory = binding.searchHistoryLinearLayout

        trackAdapterSearchHistory.onItemClickListener = this

        trackAdapter.onItemClickListener = this

        // кнопка "назад"
        val buttonSearchBack = binding.buttonSearchBack
        buttonSearchBack.setOnClickListener {
            onBackPressed()
        }

        val inputEditText = binding.editSearchWindow

        // для поиска
        val rvItems: RecyclerView = binding.rvItems
        rvItems.apply {
            adapter = trackAdapter
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }
        trackAdapter.items = trackList

        // для истории поиска
        val rvItemsSearchHistory: RecyclerView = binding.rvSearchHistoryItems
        rvItemsSearchHistory.apply {
            adapter =
                trackAdapterSearchHistory
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }

        // подписываемся на состояние SearchViewModel
        viewModel.getScreenState().observe(this) { state ->
            when(state) {
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
                val placeholderLayout: LinearLayout = binding.placeholderLinearLayout
                placeholderLayout.visibility = View.GONE
                rvItems.visibility = View.GONE
                SearchRequest()
                true
            }
            false
        }

        // кнопка "очистить поле ввода"
        val clearButton = binding.buttonCloseClearCancel
        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(inputEditText.getWindowToken(), 0)

            trackAdapter.items.clear()
            trackAdapter.notifyDataSetChanged()

            val placeholderLayout: LinearLayout = binding.placeholderLinearLayout
            placeholderLayout.visibility = View.GONE
        }

        mainHandler = Handler(Looper.getMainLooper())

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //    TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)

                val linearLayoutSearchHistory = binding.searchHistoryLinearLayout
                linearLayoutSearchHistory.visibility =
                    if (inputEditText.hasFocus() && (s?.isEmpty() == true))
                        View.VISIBLE
                    else View.GONE

                trackList.clear()
                trackAdapter.items = trackList
                trackAdapter.notifyDataSetChanged()

                val placeholderLinearLayout = binding.placeholderLinearLayout
                placeholderLinearLayout.visibility = View.GONE

                SearchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                //  TODO("Not yet implemented")
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        // отображаем LinearLayout истории поиска, если фокус находится в inputEditText и inputEditText пуст
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            linearLayoutSearchHistory.visibility =
                if (hasFocus && inputEditText.text.isEmpty())
                    View.VISIBLE
                else View.GONE
        }

        inputEditText.requestFocus()

        // кнопка "Очистить историю"
        val buttonCleanSearchHistory = binding.buttonCleanSearchHistory
        buttonCleanSearchHistory.setOnClickListener {

            viewModel.searchHistoryClean()
            trackAdapterSearchHistory.items.clear()
            trackAdapterSearchHistory.notifyDataSetChanged()

            linearLayoutSearchHistory.visibility = View.GONE

            viewModel.writeToSharedPreferences()
        }
    }

    private fun showHistory(data: List<Track>) {
        trackAdapterSearchHistory.items = data.toMutableList()
    }

    // Поисковый запрос
    private fun SearchRequest() {
        val inputEditText = binding.editSearchWindow

        if (inputEditText.text.isNotEmpty()) {
            progressBarVisibility(true)// Показываем ProgressBar перед выполнением запроса

            viewModel.loadData(inputEditText.text.toString())

            progressBarVisibility(false) // Прячем ProgressBar после выполнения запроса
        }
    }

    private fun showError(code: Int) {
        // кнопка "Обновить"
        val buttonRefresh = binding.buttonRefresh
        buttonRefresh.visibility = View.VISIBLE
        var message = ""
        if(code == -1) {
            message = getString(string.search_err)
        }
        showMessage(
            getString(string.something_went_wrong),
            message
        )
    }

    private fun showContent(data: List<Track>) { // отображение контента, полученного после запроса, т.е. состояние SearchScreenState = content
        // кнопка "Обновить"
        val buttonRefresh = binding.buttonRefresh
        buttonRefresh.visibility = View.GONE

        val rvItems: RecyclerView = binding.rvItems
        rvItems.apply {
            adapter = trackAdapter
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }
        trackList.clear()

        trackList.addAll(data)
        trackAdapter.items = trackList
        trackAdapter.notifyDataSetChanged()

        if (trackList.isEmpty()) {
            showMessage(getString(string.nothing_found), "")
        } else {
            showMessage("", "")
        }
    }

    private fun progressBarVisibility(isVisible: Boolean) {
        var progressBar = binding.progressBar

        if(isVisible) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    private fun showMessage(text: String, additionalMessage: String) {
        val placeholderLayout: LinearLayout = binding.placeholderLinearLayout
        val placeholderMessage: TextView = binding.placeholderMessage
        val placeholderImage: ImageView = binding.placeholderImage
        val rvItems: RecyclerView = binding.rvItems

        if (text.isNotEmpty()) {
            rvItems.visibility = View.GONE
            placeholderLayout.visibility = View.VISIBLE

            when (text) {
                getString(string.nothing_found) ->
                    placeholderImage.setImageResource(R.drawable.nothing_found)

                getString(string.something_went_wrong) ->
                    placeholderImage.setImageResource(R.drawable.something_went_wrong)
            }
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG).show()
            }
        } else {
            rvItems.visibility = View.VISIBLE
            placeholderLayout.visibility = View.GONE
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

         val linearLayoutSearchHistory = binding.searchHistoryLinearLayout
         outState.putString(
             SEARCH_HISTORY_VISIBILITY,
             linearLayoutSearchHistory.visibility.toString()
         )
         outState.putParcelableArrayList(
             TRACK_LIST_SEARCH_HISTORY,
            viewModel.getTrackListSearchHistory()
         )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        editString = savedInstanceState.getString(EDIT_STRING, "")
        val inputEditText = binding.editSearchWindow
        inputEditText.setText(editString)

        trackList = savedInstanceState.getParcelableArrayList(TRACK_LIST)!!
        trackAdapter.items = trackList
        trackAdapter.notifyDataSetChanged()

        hidePlaceholder()

        viewModel.setTrackListSearchHistory(savedInstanceState.getParcelableArrayList(TRACK_LIST_SEARCH_HISTORY)!!)
        trackAdapterSearchHistory.items = viewModel.getTrackListSearchHistory()
        trackAdapterSearchHistory.notifyDataSetChanged()

        val linearLayoutSearchHistory = binding.searchHistoryLinearLayout
        linearLayoutSearchHistory.visibility =
            when (savedInstanceState.getString(SEARCH_HISTORY_VISIBILITY, "0")) {
                "4" -> View.INVISIBLE
                "8" -> View.GONE
                else -> View.VISIBLE
            }
    }

    private fun hidePlaceholder() {
        val placeholderLayout: LinearLayout = binding.placeholderLinearLayout
        val rvItems: RecyclerView = binding.rvItems

        rvItems.visibility = View.VISIBLE
        placeholderLayout.visibility = View.GONE
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
            val displayIntent = Intent(this, MediaActivity::class.java)
            displayIntent.putExtra(TRACK, item)

            startActivity(displayIntent)
        }
    }

    // для поиска при задержке ввода на 2 секунды
    private fun SearchDebounce() {
        mainHandler?.removeCallbacks(searchRunnable)
        mainHandler?.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

        // Предотвращение двойного нажатия на элемент списка
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            mainHandler?.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}