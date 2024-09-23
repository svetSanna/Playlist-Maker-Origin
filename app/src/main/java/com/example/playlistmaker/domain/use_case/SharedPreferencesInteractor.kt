package com.example.playlistmaker.domain.use_case

interface SharedPreferencesInteractor {
    fun edit(str: String)
    fun getString(key: String): String?
}