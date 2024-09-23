package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(
    private val repositoryImpl: SearchHistoryRepository) : SearchHistoryInteractor
{
    override fun searchHistoryClean() {
        return repositoryImpl.searchHistoryClean()
    }

    override fun getTrackListSearchHistory(): ArrayList<Track> {
        return repositoryImpl.getTrackListSearchHistory()
    }

    override fun writeToSharedPreferences() {
        return repositoryImpl.writeToSharedPreferences()
    }

    override fun setTrackListSearchHistory(parcelableArrayList: ArrayList<Track>) {
        return repositoryImpl.setTrackListSearchHistory(parcelableArrayList)
    }

    override fun addItem(item: Track) {
        return repositoryImpl.addItem(item)
    }

    override fun itemClick(item: Track) {
        return repositoryImpl.itemClick(item)
    }
}