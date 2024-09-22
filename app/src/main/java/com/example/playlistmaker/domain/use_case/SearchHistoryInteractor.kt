package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.entity.Track

interface SearchHistoryInteractor {
        fun searchHistoryClean()
        fun getTrackListSearchHistory(): ArrayList<Track>
        fun writeToSharedPreferences()
        fun setTrackListSearchHistory(parcelableArrayList: ArrayList<Track>)
        fun addItem(item: Track)
        fun itemClick(item: Track)
}