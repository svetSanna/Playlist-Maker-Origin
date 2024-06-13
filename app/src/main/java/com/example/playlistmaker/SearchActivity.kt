package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var editString: String = ""
    private var trackList: ArrayList<Track> = arrayListOf()

    // базовый URL для Retrofit
    private val baseUrlStr = "https://itunes.apple.com"  //https://itunes.apple.com/search?entity=song&term="мама"

    // подключаем библиотеку Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrlStr)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // получаем реализацию нашего com.example.playlistmaker.TrackApi
    private val trackApiService = retrofit.create(TrackApi::class.java) //val trackApiService = retrofit.create<TrackApi>()

    // создаем адаптер для Track
    private val trackAdapter = TrackAdapter()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search)

        // кнопка "назад"
        val buttonSearchBack = findViewById<ImageView>(id.button_search_back)
        buttonSearchBack.setOnClickListener {
            onBackPressed()
        }

        val inputEditText = findViewById<EditText>(id.edit_search_window)

        // кнопка "очистить поле ввода"
        val clearButton = findViewById<ImageView>(id.button_close_clear_cancel)

        val rvItems: RecyclerView = findViewById(R.id.rvItems)
        rvItems.apply{
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }
        trackAdapter.items = trackList

        // кнопка "Обновить"
        val buttonRefresh = findViewById<Button>(id.buttonRefresh)
        buttonRefresh.visibility=View.GONE
        buttonRefresh.setOnClickListener {
            inputEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        clearButton.setOnClickListener{
            inputEditText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(inputEditText.getWindowToken(), 0)

            trackAdapter.items.clear()
            trackAdapter.notifyDataSetChanged()

            val placeholderLayout: LinearLayout = findViewById(id.placeholderLinearLayout)
            placeholderLayout.visibility = View.GONE
        }

        inputEditText.requestFocus()

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //    TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editString = s?.toString()?:""
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
              //  TODO("Not yet implemented")
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        //Чтобы обработать нажатие на кнопку Done, к соответствующему экземпляру EditText нужно добавить специального слушателя:
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Поисковый запрос
                if(inputEditText.text.isNotEmpty()){
                    trackApiService.search(inputEditText.text.toString()).enqueue(object: Callback<TrackResponse>{
                        override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                            if (response.code() == 200) {
                                buttonRefresh.visibility=View.GONE
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
                                buttonRefresh.visibility=View.VISIBLE
                                showMessage(getString(string.something_went_wrong), response.code().toString())
                            }
                        }
                        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                            buttonRefresh.visibility=View.VISIBLE
                            showMessage(getString(string.something_went_wrong), t.message.toString())
                        }
                    })
                }
                true
            }
            false
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

            when(text){
                getString(string.nothing_found) ->
                    placeholderImage.setImageResource(R.drawable.nothing_found)
                getString(string.something_went_wrong)->
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
        return if (s.isNullOrEmpty()){
            View.GONE
        } else{
            View.VISIBLE
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("editString", editString)

        outState.putParcelableArrayList("track_list", trackList as ArrayList<out Parcelable?>?)
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
    }

    override fun onResume() { // решила сделать для сохранения cписка найденных пересен, например, при повороте телефона
        super.onResume()
    }
}