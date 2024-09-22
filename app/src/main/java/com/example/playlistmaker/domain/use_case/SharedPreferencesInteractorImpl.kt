package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.repository.SharedPreferencesRepository

class SharedPreferencesInteractorImpl(private val repository: SharedPreferencesRepository) : SharedPreferencesInteractor {
    override fun edit(str: String) {
        repository.edit(str)
    }

    override fun getString(key: String): String? {
        return repository.getString(key)
    }
}