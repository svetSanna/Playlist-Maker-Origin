package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryInteractor(
    private val repositoryImpl: SearchHistoryRepository
) {
    fun searchHistoryClean() {
        return repositoryImpl.searchHistoryClean()
    }

    fun getTrackListSearchHistory(): ArrayList<Track> {
        return repositoryImpl.getTrackListSearchHistory()
    }

    fun writeToSharedPreferences() {
        return repositoryImpl.writeToSharedPreferences()
    }

    fun setTrackListSearchHistory(parcelableArrayList: ArrayList<Track>) {
        return repositoryImpl.setTrackListSearchHistory(parcelableArrayList)
    }

    fun addItem(item: Track) {
        return repositoryImpl.addItem(item)
    }

    fun itemClick(item: Track) {
        return repositoryImpl.itemClick(item)
    }
}