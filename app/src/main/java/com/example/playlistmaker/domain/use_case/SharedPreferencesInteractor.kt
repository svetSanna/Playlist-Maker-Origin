package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.repository.SharedPreferencesRepository

class SharedPreferencesInteractor(private val repository: SharedPreferencesRepository) {
    fun edit(str: String) {
        repository.edit(str)
    }

    fun getString(key: String): String? {
        return repository.getString(key)
    }
}