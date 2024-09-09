package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.repository.SharedPreferencesRepository

class SharedPreferencesInteractor(private val repository: SharedPreferencesRepository) {
    //private val executor = Executors.newSingleThreadExecutor()
    fun edit(str: String) {
        //  executor.execute {
        repository.edit(str)
        //  }
    }

    fun getString(key: String): String? {
        return repository.getString(key)
    }
}