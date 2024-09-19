package com.example.playlistmaker.presentation.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.state.SearchScreenState

class SearchViewModel() : ViewModel() {
    private var getTrackListUseCase = Creator.provideGetTrackListUseCase()
    private var historyInteractor = Creator.provideGetSearchHistoryInteractor()

    private val state = MutableLiveData<SearchScreenState>()
        init{
        loadData("")
    }
    fun getScreenState() : LiveData<SearchScreenState> = state

    fun loadData(inputSearchEditText: String) {
        state.postValue(SearchScreenState.Loading)
        Log.i("MyTest", "SearchViewModel.loadData - 1")

        // если поле ввода пустое, то на экране состояние "история поиска"
        if(inputSearchEditText.isEmpty()){
            // получаем историю поиска из SharedPreferences с помощью интерактора
            val contentHistory = SearchScreenState.ContentHistory(historyInteractor.getTrackListSearchHistory())
            // и кладем ее в состояние contentHistory
            state.postValue(contentHistory)
            Log.i("MyTest", "SearchViewModel.loadData.contentHistory")
        }
        else { // если поле ввода не пустое, то с помощью UseCase делаем запрос в сеть и либо находим треки, либо получаем ошибку
            getTrackListUseCase(
                str = inputSearchEditText,
                consumer = object : Consumer<List<Track>> {
                    override fun consume(data: ConsumerData<List<Track>>) {
                        when (data) {
                            is ConsumerData.Error -> {
                                val error = SearchScreenState.Error(data.code)
                                state.postValue(error)
                                Log.i("MyTest", "SearchViewModel.loadData.contentError")
                            }

                            is ConsumerData.Data -> {
                                // val productDetailsInfo = ProductDetailsInfoMapper.map(data.value)
                                val content = SearchScreenState.Content(data.value)//productDetailsInfo)
                                state.postValue(content)
                                Log.i("MyTest", "SearchViewModel.loadData.content")
                            }
                        }
                    }
                }
                /*    getTrackListUseCase(
            str = inputSearchEditText,
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

                                    if (trackList.isEmpty()) {
                                        showMessage(getString(R.string.nothing_found), "")
                                    } else {
                                        showMessage("", "")
                                    }
                                }
                                is ConsumerData.Error -> {
                                    buttonRefresh.visibility = View.VISIBLE
                                    var message = ""
                                    if(data.code == -1) {
                                        message = getString(R.string.search_err)
                                    }
                                    showMessage(
                                        getString(R.string.something_went_wrong),
                                        message
                                    )
                                }
                            }
                        }
                        responseRunnable = newDetailsRunnable
                        mainHandler?.post(newDetailsRunnable)
                    }
                }*/
            )
        }
    }

    fun getTrackListSearchHistory() : ArrayList<Track>{
        return historyInteractor.getTrackListSearchHistory()
    }
    fun searchHistoryClean() {
        historyInteractor.searchHistoryClean()
    }

    fun writeToSharedPreferences() {
        historyInteractor.writeToSharedPreferences()
    }

    fun setTrackListSearchHistory(parcelableArrayList: ArrayList<Track>) {
        historyInteractor.setTrackListSearchHistory(parcelableArrayList)
    }

    fun addItemToTrackListSearchHistory(item: Track) {
        historyInteractor.addItem(item)
    }
    /* companion object { // фабрика нужна, чтобы передать параметр в конструктор ViewModel
         fun factory(inputSearchEditText: String) = viewModelFactory {
             initializer {
                 SearchViewModel(inputSearchEditText)
             }
         }
     }*/
  /*  fun getTrackList(str: String){
      loadData(str)
    }*/
}