package com.example.playlistmaker.ui.SearchActivity

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.ui.SEARCH_HISTORY_KEY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory (val sharedPrefs: android.content.SharedPreferences){
    var trackListSearchHistory: ArrayList<Track> = arrayListOf()

    // создаем адаптер для Track для истории поиска
    var trackAdapterSearchHistory = TrackAdapter()

     init{
         val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "")
         when (json) {
            "" -> {
                trackListSearchHistory.clear()
               // linearLayoutSearchHistory.visibility = View.GONE
            }
            else -> {
                val trackListType = object : TypeToken<ArrayList<Track>>() {}.type
                trackListSearchHistory = Gson().fromJson(json, trackListType)
            }
        }
        trackAdapterSearchHistory.items = trackListSearchHistory
    }

    fun clean()
    {
        trackListSearchHistory.clear()
        trackAdapterSearchHistory.items = trackListSearchHistory
        trackAdapterSearchHistory.notifyDataSetChanged()
    }

    fun writeToSharedPreferences(){
        val json = Gson().toJson(trackListSearchHistory)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun addItem(item: Track){
        var itemSearchHistory = trackListSearchHistory.firstOrNull { it.trackId == item.trackId }
        if (itemSearchHistory!=null)
            trackListSearchHistory.remove(itemSearchHistory)
        trackListSearchHistory.add(0,item)

        if(trackListSearchHistory.size>10)
            trackListSearchHistory.removeAt(10)//(trackListSearchHistory[10])

        trackAdapterSearchHistory.items = trackListSearchHistory
        trackAdapterSearchHistory.notifyDataSetChanged()
    }
}