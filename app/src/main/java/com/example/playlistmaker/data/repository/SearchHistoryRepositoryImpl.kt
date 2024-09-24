package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.history.SearchHistory
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(var searchHistory: SearchHistory) : SearchHistoryRepository {
    //var searchHistory = SearchHistory()

    override fun searchHistoryClean() {
        searchHistory.clean()
    }

    override fun getTrackListSearchHistory(): ArrayList<Track> {
        return searchHistory.trackListSearchHistory
    }

    override fun writeToSharedPreferences() {
        searchHistory.writeToSharedPreferences()
    }

    override fun setTrackListSearchHistory(parcelableArrayList: ArrayList<Track>) {
        searchHistory.trackListSearchHistory = parcelableArrayList
    }

    override fun addItem(item: Track) {
        searchHistory.addItem(item)
    }

    override fun itemClick(item: Track) {
        var itemSearchHistory = getTrackListSearchHistory().firstOrNull { it.trackId == item.trackId }

        if (itemSearchHistory != null)
            getTrackListSearchHistory().remove(itemSearchHistory)

        getTrackListSearchHistory().add(0, item)

        if (getTrackListSearchHistory().size > 10)
            getTrackListSearchHistory().removeAt(10)
    }
}