package com.example.playlistmaker.presentation.view_model

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

    init {
        loadData("")
    }

    fun getScreenState(): LiveData<SearchScreenState> = state

    fun loadData(inputSearchEditText: String) {
        state.postValue(SearchScreenState.Loading)

        // если поле ввода пустое, то на экране состояние "история поиска"
        if (inputSearchEditText.isEmpty()) {
            // получаем историю поиска из SharedPreferences с помощью интерактора
            val contentHistory =
                SearchScreenState.ContentHistory(historyInteractor.getTrackListSearchHistory())
            // и кладем ее в состояние contentHistory
            state.postValue(contentHistory)
        } else { // если поле ввода не пустое, то с помощью UseCase делаем запрос в сеть и либо находим треки, либо получаем ошибку
            getTrackListUseCase(
                str = inputSearchEditText,
                consumer = object : Consumer<List<Track>> {
                    override fun consume(data: ConsumerData<List<Track>>) {
                        when (data) {
                            is ConsumerData.Error -> {
                                val error = SearchScreenState.Error(data.code)
                                state.postValue(error)
                            }

                            is ConsumerData.Data -> {
                                val content =
                                    SearchScreenState.Content(data.value)//productDetailsInfo)
                                state.postValue(content)
                            }
                        }
                    }
                }
            )
        }
    }

    fun getTrackListSearchHistory(): ArrayList<Track> {
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

    fun itemClick(item: Track) {
        historyInteractor.itemClick(item)
    }
}