package com.example.playlistmaker.data.repository

import com.example.playlistmaker.THEME_SWITCH_KEY
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.repository.SharedPreferencesRepository

class SharedPreferencesRepositoryImpl : SharedPreferencesRepository {
    private val sharedPrefs = Creator.provideSharedPreferences()
    override fun edit(str: String) {
        sharedPrefs.edit()
            .putString(THEME_SWITCH_KEY, str)
            .apply()
    }

    override fun getString(key: String): String? {
        return sharedPrefs.getString(key, "")
    }
}