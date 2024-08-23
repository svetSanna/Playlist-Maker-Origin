package com.example.playlistmaker.domain.repository

interface SharedPreferencesRepository {
    fun edit(str: String)
    fun getString(key: String): String?
}