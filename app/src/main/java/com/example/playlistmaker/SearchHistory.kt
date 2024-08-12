package com.example.playlistmaker

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.ui.SearchActivity.TrackAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory (val sharedPrefs: android.content.SharedPreferences){
    var trackListSearchHistory: ArrayList<Track> = arrayListOf()

    /*(Track( // 1 элемент
        trackId = 1,
        trackName = "Smells Like Teen Spirit",
        artistName = "Nirvana",
        trackTimeMillis = 3000000,
        artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
    ),
    Track( // 2 элемент
        trackId = 2,
        trackName = "Billie Jean",
        artistName = "Michael Jackson",
        trackTimeMillis = 261000,
        artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
    ),
    Track( // 3 элемент
        trackId = 3,
        trackName = "Stayin' Alive",
        artistName = "Bee Gees",
        trackTimeMillis = 246000,
        artworkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
    ),
    Track( // 4 элемент
        trackId = 4,
        trackName = "Whole Lotta Love",
        artistName = "Led Zeppelin",
        trackTimeMillis = 319800,
        artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
    ),
    Track( // 5 элемент
        trackId = 5,
        trackName = "Sweet Child O'Mine",
        artistName = "Guns N' Roses",
        trackTimeMillis = 301800,
        artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
    ),
    Track( // 6 элемент
        trackId = 6,
        trackName = "Smells Like Teen Spirit 2",
        artistName = "Nirvana",
        trackTimeMillis = 3000000,
        artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
    ),
    Track( // 7 элемент
        trackId = 7,
        trackName = "Billie Jean 2",
        artistName = "Michael Jackson",
        trackTimeMillis = 261000,
        artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
    ),
    Track( // 8 элемент
        trackId = 8,
        trackName = "Stayin' Alive 2",
        artistName = "Bee Gees",
        trackTimeMillis = 246000,
        artworkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
    ),
    Track( // 9 элемент
        trackId = 9,
        trackName = "Whole Lotta Love 2",
        artistName = "Led Zeppelin",
        trackTimeMillis = 319800,
        artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
    ),
    Track( // 10 элемент
        trackId = 10,
        trackName = "Sweet Child O'Mine 2",
        artistName = "Guns N' Roses",
        trackTimeMillis = 301800,
        artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
    ))*/
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
    // Toast.makeText(this, "Сохранили значение ${editText.editableText}", Toast.LENGTH_SHORT).show()
}