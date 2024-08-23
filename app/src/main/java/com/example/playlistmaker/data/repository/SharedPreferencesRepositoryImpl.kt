package com.example.playlistmaker.data.repository

import com.example.playlistmaker.THEME_SWITCH_KEY
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.repository.SharedPreferencesRepository

class SharedPreferencesRepositoryImpl: SharedPreferencesRepository {
    override fun editSharePreferences(str: String) {
        val sharedPrefs = Creator.provideSharedPreferences()
        sharedPrefs.edit()
            .putString(THEME_SWITCH_KEY, str)
            .apply()
    }
}