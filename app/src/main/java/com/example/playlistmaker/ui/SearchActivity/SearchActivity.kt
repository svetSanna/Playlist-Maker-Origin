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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.R.id
import com.example.playlistmaker.R.layout
import com.example.playlistmaker.R.string
import com.example.playlistmaker.TRACK
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.use_case.SearchHistoryInteractor
import com.example.playlistmaker.ui.MediaActivity.MediaActivity

class SearchActivity : AppCompatActivity(), TrackViewHolder.OnItemClickListener {
    private var editString: String = ""
    private var trackList: ArrayList<Track> = arrayListOf()

    /* // базовый URL для Retrofit
     private val baseUrlStr =
         "https://itunes.apple.com"  //https://itunes.apple.com/search?entity=song&term="мама"

     // подключаем библиотеку Retrofit
     private val retrofit = Retrofit.Builder()
         .baseUrl(baseUrlStr)
         .addConverterFactory(GsonConverterFactory.create())
         .build()

     // получаем реализацию нашего com.example.playlistmaker.data.network.TrackApi
     private val trackApiService =
         retrofit.create(TrackApi::class.java) //val trackApiService = retrofit.create<TrackApi>()
 */
    // создаем адаптер для Track
    private val trackAdapter = TrackAdapter()

    // создаем адаптер для Track для истории поиска
    private var trackAdapterSearchHistory = TrackAdapter() //1

    //lateinit var searchHistory: SearchHistory

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY =
            2000L     // для поиска при задержке ввода на 2 секунды
        private const val CLICK_DEBOUNCE_DELAY =
            1000L      // для предотвращения нажатия два раза подряд на элемент списка
    }

    // Создаём Handler, привязанный к ГЛАВНОМУ потоку
    private val searchRunnable = Runnable { SearchRequest() }

    private var mainHandler: Handler? = null

    private var isClickAllowed = true

    private val getTrackListUseCase = Creator.provideGetTrackListUseCase()
    private var responseRunnable: Runnable? =
        null // для того, чтобы обновление UI относительно данных,
    // которые пришли из другого потока, происходило в главном потоке.

    private lateinit var historyInteractor: SearchHistoryInteractor

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search)

        /*searchHistory = SearchHistory(
             getSharedPreferences(
                 PLAYLISTMAKER_PREFERENCES,
                 MODE_PRIVATE // MODE_PRIVATE - чтобы данные были доступны только нашему приложению
             )
         ) */  //n1

        /*Creator.initApplication(this)
        val sharedPrefs = Creator.provideSharedPreferences()
        searchHistory = SearchHistory(sharedPrefs)*/ //v1

        //Creator.initApplication(this) //v1 //p2
        //val sharedPrefs = Creator.provideSharedPreferences() //v1 //p3

        //searchHistory = Creator.provideGetSearchHistoryInteractor(sharedPrefs) //v1

        //trackAdapterSearchHistory.items = searchHistory.trackListSearchHistory //2 //v1
        historyInteractor = Creator.provideGetSearchHistoryInteractor() // historyInteractor = Creator.provideGetSearchHistoryInteractor(sharedPrefs) //v1   //p3
        trackAdapterSearchHistory.items = historyInteractor.getTrackListSearchHistory() //2 //v1
        trackAdapterSearchHistory.onItemClickListener =
            this // searchHistory.trackAdapterSearchHistory.onItemClickListener = this //2

        trackAdapter.onItemClickListener = this

        // LinearLayout истории поиска
        val linearLayoutSearchHistory = findViewById<LinearLayout>(id.searchHistoryLinearLayout)

        // Получаем историю поиска из SharedPreferences, а если
        // ничего туда не успели сохранить, то список пустой и не отображается
        if (historyInteractor.getTrackListSearchHistory()
                .isEmpty()
        )//if (searchHistory.trackListSearchHistory.isEmpty()) //v1
            linearLayoutSearchHistory.visibility = View.GONE

        // кнопка "назад"
        val buttonSearchBack = findViewById<ImageView>(id.button_search_back)
        buttonSearchBack.setOnClickListener {
            onBackPressed()
        }

        val inputEditText = findViewById<EditText>(id.edit_search_window)

        val rvItems: RecyclerView = findViewById(R.id.rvItems)
        rvItems.apply {
            adapter = trackAdapter
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }
        trackAdapter.items = trackList

        // для истории поиска
        val rvItemsSearchHistory: RecyclerView = findViewById(R.id.rvSearchHistoryItems)
        rvItemsSearchHistory.apply {
            adapter =
                trackAdapterSearchHistory //adapter = searchHistory.trackAdapterSearchHistory //3
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }

        // кнопка "Обновить"
        val buttonRefresh = findViewById<Button>(id.buttonRefresh)
        buttonRefresh.visibility = View.GONE
        buttonRefresh.setOnClickListener {
            inputEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val placeholderLayout: LinearLayout = findViewById(id.placeholderLinearLayout)
                placeholderLayout.visibility = View.GONE
                rvItems.visibility = View.GONE

                SearchRequest()
                true
            }
            false
        }

        // кнопка "очистить поле ввода"
        val clearButton = findViewById<ImageView>(id.button_close_clear_cancel)
        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(inputEditText.getWindowToken(), 0)

            trackAdapter.items.clear()
            trackAdapter.notifyDataSetChanged()

            val placeholderLayout: LinearLayout = findViewById(id.placeholderLinearLayout)
            placeholderLayout.visibility = View.GONE
        }

        mainHandler = Handler(Looper.getMainLooper())

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //    TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editString = s?.toString() ?: ""
                clearButton.visibility = clearButtonVisibility(s)

                val linearLayoutSearchHistory =
                    findViewById<LinearLayout>(id.searchHistoryLinearLayout)
                linearLayoutSearchHistory.visibility =
                    if (inputEditText.hasFocus() && (s?.isEmpty() == true) && !historyInteractor.getTrackListSearchHistory()
                            .isEmpty()
                    ) // if (inputEditText.hasFocus() && (s?.isEmpty() == true) && !searchHistory.trackListSearchHistory.isEmpty()) //v1
                        View.VISIBLE
                    else View.GONE

                trackList.clear()
                trackAdapter.items = trackList
                trackAdapter.notifyDataSetChanged()

                val placeholderLinearLayout = findViewById<LinearLayout>(id.placeholderLinearLayout)
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
                    //if (hasFocus && inputEditText.text.isEmpty() && !searchHistory.trackListSearchHistory.isEmpty()) //v1
                if (hasFocus && inputEditText.text.isEmpty() && !historyInteractor.getTrackListSearchHistory()
                        .isEmpty()
                )
                    View.VISIBLE
                else View.GONE
        }

        inputEditText.requestFocus()

        // кнопка "Очистить историю"
        val buttonCleanSearchHistory = findViewById<Button>(id.buttonCleanSearchHistory)
        buttonCleanSearchHistory.setOnClickListener {

            historyInteractor.searchHistoryClean() //searchHistory.clean() //v1
            trackAdapterSearchHistory.items =
                historyInteractor.getTrackListSearchHistory() //searchHistory.trackListSearchHistory //4 //v1
            trackAdapterSearchHistory.notifyDataSetChanged()         //4

            linearLayoutSearchHistory.visibility = View.GONE

            historyInteractor.writeToSharedPreferences() //searchHistory.writeToSharedPreferences() //v1
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        val placeholderLayout: LinearLayout = findViewById(id.placeholderLinearLayout)
        val placeholderMessage: TextView = findViewById(id.placeholderMessage)
        val placeholderImage: ImageView = findViewById(id.placeholderImage)
        val rvItems: RecyclerView = findViewById(id.rvItems)

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
        outState.putString("editString", editString)

        outState.putParcelableArrayList("track_list", trackList as ArrayList<out Parcelable?>?)

        val linearLayoutSearchHistory = findViewById<LinearLayout>(id.searchHistoryLinearLayout)
        outState.putString(
            "search_history_visibility",
            linearLayoutSearchHistory.visibility.toString()
        )
        /*outState.putParcelableArrayList(
            "track_list_search_history",
            searchHistory.trackListSearchHistory as ArrayList<out Parcelable?>?
        )*/ //v1
        outState.putParcelableArrayList(
            "track_list_search_history",
            historyInteractor.getTrackListSearchHistory() as ArrayList<out Parcelable?>?
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        editString = savedInstanceState.getString("editString", "")
        val inputEditText = findViewById<EditText>(id.edit_search_window)
        inputEditText.setText(editString)

        trackList = savedInstanceState.getParcelableArrayList("track_list")!!
        trackAdapter.items = trackList
        trackAdapter.notifyDataSetChanged()

        hidePlaceholder()

        /*searchHistory.trackListSearchHistory =
            savedInstanceState.getParcelableArrayList("track_list_search_history")!!
        trackAdapterSearchHistory.items = searchHistory.trackListSearchHistory //searchHistory.trackAdapterSearchHistory.items = searchHistory.trackListSearchHistory  //5
        trackAdapterSearchHistory.notifyDataSetChanged() //searchHistory.trackAdapterSearchHistory.notifyDataSetChanged()                        //5 */ //v1

        historyInteractor.setTrackListSearchHistory(
            savedInstanceState.getParcelableArrayList("track_list_search_history")!!
        )   //v1
        trackAdapterSearchHistory.items =
            historyInteractor.getTrackListSearchHistory()//searchHistory.trackListSearchHistory //searchHistory.trackAdapterSearchHistory.items = searchHistory.trackListSearchHistory  //5  //v1
        trackAdapterSearchHistory.notifyDataSetChanged() //searchHistory.trackAdapterSearchHistory.notifyDataSetChanged()                        //5  //v1


        val linearLayoutSearchHistory = findViewById<LinearLayout>(id.searchHistoryLinearLayout)
        linearLayoutSearchHistory.visibility =
            when (savedInstanceState.getString("search_history_visibility", "0")) {
                "4" -> View.INVISIBLE
                "8" -> View.GONE
                else -> View.VISIBLE
            }
    }

    private fun hidePlaceholder() {
        val placeholderLayout: LinearLayout = findViewById(id.placeholderLinearLayout)
        val rvItems: RecyclerView = findViewById(id.rvItems)

        rvItems.visibility = View.VISIBLE
        placeholderLayout.visibility = View.GONE
    }

    override fun onItemClick(item: Track) {
        if (clickDebounce()) { // если между нажатиями на элемент прошло не меньше 1 секунды
            /*            var itemSearchHistory =
                            searchHistory.trackListSearchHistory.firstOrNull { it.trackId == item.trackId }
                        if (itemSearchHistory != null)
                            searchHistory.trackListSearchHistory.remove(itemSearchHistory)
                        searchHistory.trackListSearchHistory.add(0, item)

                        if (searchHistory.trackListSearchHistory.size > 10)
                            searchHistory.trackListSearchHistory.removeAt(10)
            */ //v1
            var itemSearchHistory =
                historyInteractor.getTrackListSearchHistory()
                    .firstOrNull { it.trackId == item.trackId } //v1
            if (itemSearchHistory != null)
                historyInteractor.getTrackListSearchHistory().remove(itemSearchHistory)  //v1
            historyInteractor.getTrackListSearchHistory().add(0, item)

            if (historyInteractor.getTrackListSearchHistory().size > 10)
                historyInteractor.getTrackListSearchHistory().removeAt(10)  //v1

            // trackAdapterSearchHistory.items = searchHistory.trackListSearchHistory //searchHistory.trackAdapterSearchHistory.items = searchHistory.trackListSearchHistory //6 //v1
            trackAdapterSearchHistory.items = historyInteractor.getTrackListSearchHistory() //v1
            trackAdapterSearchHistory.notifyDataSetChanged() //searchHistory.trackAdapterSearchHistory.notifyDataSetChanged() //6

            //searchHistory.addItem(item) //v1
            historyInteractor.addItem(item) //v1
            //trackAdapterSearchHistory.items = searchHistory.trackListSearchHistory //7 //v1
            trackAdapterSearchHistory.items = historyInteractor.getTrackListSearchHistory() //v1
            trackAdapterSearchHistory.notifyDataSetChanged() //7

            //searchHistory.writeToSharedPreferences() //v1
            historyInteractor.writeToSharedPreferences() //v1

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

    // Поисковый запрос
    private fun SearchRequest() {
        val inputEditText = findViewById<EditText>(id.edit_search_window)
        val buttonRefresh = findViewById<Button>(id.buttonRefresh)
        var progressBar = findViewById<ProgressBar>(id.progressBar)

        //progressBar.visibility = View.VISIBLE // Показываем ProgressBar перед выполнением запроса

        if (inputEditText.text.isNotEmpty()) {
            progressBar.visibility =
                View.VISIBLE // Показываем ProgressBar перед выполнением запроса

            getTrackListUseCase(
                str = inputEditText.text.toString(),
                consumer = object : Consumer<List<Track>> {
                    override fun consume(data: ConsumerData<List<Track>>) {
                        val currentRunnable = responseRunnable
                        if (currentRunnable != null) {
                            mainHandler?.removeCallbacks(currentRunnable)
                        }

                        val newDetailsRunnable = Runnable {
                            progressBar.visibility =
                                View.GONE // Прячем ProgressBar после выполнения запроса

                            when (data) {
                                is ConsumerData.Data -> {
                                    buttonRefresh.visibility = View.GONE
                                    trackList.clear()

                                    trackList.addAll(data.value)
                                    trackAdapter.items = trackList
                                    trackAdapter.notifyDataSetChanged()

                                    /*if (response.body()?.results?.isNotEmpty() == true) {
                                    trackList.addAll(response.body()?.results!!)
                                    trackAdapter.items = trackList
                                    trackAdapter.notifyDataSetChanged()
                                }*/
                                    if (trackList.isEmpty()) {
                                        showMessage(getString(string.nothing_found), "")
                                    } else {
                                        showMessage("", "")
                                    }
                                }

                                is ConsumerData.Error -> {
                                    //  progressBar.visibility =
                                    //     View.GONE // Прячем ProgressBar после выполнения запроса с ошибкой
                                    buttonRefresh.visibility = View.VISIBLE
                                    showMessage(
                                        getString(string.something_went_wrong),
                                        data.message
                                        //response.code().toString()//t.message.toString()
                                    )
                                }
                            }
                        }
                        responseRunnable = newDetailsRunnable
                        mainHandler?.post(newDetailsRunnable)
                    }
                }
            )

            /*trackApiService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackSearchResponse> {
                    override fun onResponse(
                        call: Call<TrackSearchResponse>,
                        response: Response<TrackSearchResponse>
                    ) {
                        progressBar.visibility =
                            View.GONE // Прячем ProgressBar после успешного выполнения запроса

                        if (response.code() == 200) {
                            buttonRefresh.visibility = View.GONE
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                trackAdapter.items = trackList
                                trackAdapter.notifyDataSetChanged()
                            }
                            if (trackList.isEmpty()) {
                                showMessage(getString(string.nothing_found), "")
                            } else {
                                showMessage("", "")
                            }
                        } else {
                            buttonRefresh.visibility = View.VISIBLE
                            showMessage(
                                getString(string.something_went_wrong),
                                response.code().toString()
                            )
                        }
                    }

                    override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                        progressBar.visibility =
                            View.GONE // Прячем ProgressBar после выполнения запроса с ошибкой
                        buttonRefresh.visibility = View.VISIBLE
                        showMessage(
                            getString(string.something_went_wrong),
                            t.message.toString()
                        )
                    }
                }
                )*/
        }

        /* if (inputEditText.text.isNotEmpty()) {
             trackApiService.search(inputEditText.text.toString())
                 .enqueue(object : Callback<TrackSearchResponse> {
                     override fun onResponse(
                         call: Call<TrackSearchResponse>,
                         response: Response<TrackSearchResponse>
                     ) {
                         progressBar.visibility =
                             View.GONE // Прячем ProgressBar после успешного выполнения запроса

                         if (response.code() == 200) {
                             buttonRefresh.visibility = View.GONE
                             trackList.clear()
                             if (response.body()?.results?.isNotEmpty() == true) {
                                 trackList.addAll(response.body()?.results!!)
                                 trackAdapter.items = trackList
                                 trackAdapter.notifyDataSetChanged()
                             }
                             if (trackList.isEmpty()) {
                                 showMessage(getString(string.nothing_found), "")
                             } else {
                                 showMessage("", "")
                             }
                         } else {
                             buttonRefresh.visibility = View.VISIBLE
                             showMessage(
                                 getString(string.something_went_wrong),
                                 response.code().toString()
                             )
                         }
                     }

                     override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                         progressBar.visibility =
                             View.GONE // Прячем ProgressBar после выполнения запроса с ошибкой
                         buttonRefresh.visibility = View.VISIBLE
                         showMessage(
                             getString(string.something_went_wrong),
                             t.message.toString()
                         )
                     }
                 }
                 )
         }*/
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

    override fun onDestroy() {
        // перед тем как уничтожить нашу activity, мы должны проверить,
        // если наш consumerRunnable не равен null, то мы можем удалить
        // его из handler
        val currentRunnable = responseRunnable
        if (currentRunnable != null) {
            mainHandler?.removeCallbacks(currentRunnable)
        }

        super.onDestroy()
    }
}