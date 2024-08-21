package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.data.history.SearchHistory
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(sharedPrefs: SharedPreferences) : SearchHistoryRepository {
    var searchHistory = SearchHistory(sharedPrefs)

    /*override fun getSearchHistory(sharedPrefs: SharedPreferences): SearchHistory {
        return SearchHistory(sharedPrefs)
    }
*/
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
        searchHistory.trackListSearchHistory = parcelableArrayList //as ArrayList<Track>
    }

    override fun addItem(item: Track) {
        searchHistory.addItem(item)
    }
}