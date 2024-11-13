package com.example.playlistmaker.presentation.view_model

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.use_case.GetTrackListUseCase
import com.example.playlistmaker.domain.use_case.SearchHistoryInteractor
import com.example.playlistmaker.presentation.state.SearchScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel( private var getTrackListUseCase : GetTrackListUseCase,
                       private var historyInteractor : SearchHistoryInteractor
) : ViewModel() {
    private var latestSearchText: String? = null //
    companion object { //
        private const val SEARCH_DEBOUNCE_DELAY = 2000L//
    }//

    private val state = MutableLiveData<SearchScreenState>()
    init {
        loadData("")
    }
    private val mySearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        loadData(changedText)
    }

    fun <T> debounce(delayMillis: Long,
                     coroutineScope: CoroutineScope,
                     useLastParam: Boolean,
                     action: (T) -> Unit): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            if (useLastParam) {
                debounceJob?.cancel()
            }
            if (debounceJob?.isCompleted != false || useLastParam) {
                debounceJob = coroutineScope.launch {
                    delay(delayMillis)
                    action(param)
                }
            }
        }
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
                                val content = SearchScreenState.Content(data.value)//productDetailsInfo)
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

    // для поиска при задержке ввода на 2 секунды
    public fun searchDebounce(changedText: String) {

        if (latestSearchText != changedText) {
            latestSearchText = changedText
            mySearchDebounce(changedText)
        }
    }
}