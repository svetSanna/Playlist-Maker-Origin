package com.example.playlistmaker.data.history

import android.content.SharedPreferences
import com.example.playlistmaker.SEARCH_HISTORY_KEY
import com.example.playlistmaker.domain.entity.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPrefs: SharedPreferences, private val json: Gson) {
    var trackListSearchHistory: ArrayList<Track> = arrayListOf()

    init {
        val jsonString = sharedPrefs.getString(SEARCH_HISTORY_KEY, "")
        when (jsonString) {
            "" -> {
                trackListSearchHistory.clear()
            }

            else -> {
                val trackListType = object : TypeToken<ArrayList<Track>>() {}.type
                trackListSearchHistory = json.fromJson(jsonString, trackListType)
            }
        }
    }

    fun clean() {
        trackListSearchHistory.clear()
    }

    fun writeToSharedPreferences() {
        val jsonString = json.toJson(trackListSearchHistory)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, jsonString)
            .apply()
    }

    fun addItem(item: Track) {
        var itemSearchHistory = trackListSearchHistory.firstOrNull { it.trackId == item.trackId }
        if (itemSearchHistory != null)
            trackListSearchHistory.remove(itemSearchHistory)
        trackListSearchHistory.add(0, item)

        if (trackListSearchHistory.size > 10)
            trackListSearchHistory.removeAt(10)
    }
}