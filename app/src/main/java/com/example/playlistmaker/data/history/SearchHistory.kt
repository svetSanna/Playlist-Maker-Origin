package com.example.playlistmaker.data.history

import com.example.playlistmaker.SEARCH_HISTORY_KEY
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.entity.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//class SearchHistory (val sharedPrefs: android.content.SharedPreferences){ //p3
class SearchHistory (){
    var trackListSearchHistory: ArrayList<Track> = arrayListOf()
    private val sharedPrefs = Creator.provideSharedPreferences()

    // создаем адаптер для Track для истории поиска
    //var trackAdapterSearchHistory = TrackAdapter() //1

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
        // trackAdapterSearchHistory.items = trackListSearchHistory //2
    }

    fun clean()
    {
        trackListSearchHistory.clear()
        //        trackAdapterSearchHistory.items = trackListSearchHistory //4
        //        trackAdapterSearchHistory.notifyDataSetChanged() //4
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

        //trackAdapterSearchHistory.items = trackListSearchHistory //7
        //trackAdapterSearchHistory.notifyDataSetChanged() //7
    }
}