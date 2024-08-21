package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entity.Track

interface SearchHistoryRepository {
   // fun getSearchHistory(sharedPrefs: SharedPreferences): SearchHistory
    fun searchHistoryClean()
    fun getTrackListSearchHistory() : ArrayList<Track>
    fun writeToSharedPreferences()
    fun setTrackListSearchHistory(parcelableArrayList: ArrayList<Track>)
    fun addItem(item: Track){}
}