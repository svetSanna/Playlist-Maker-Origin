package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.repository.SharedPreferencesRepository
import java.util.concurrent.Executors

class EditSharedPreferencesUseCase(private val repository: SharedPreferencesRepository) {
    private val executor = Executors.newSingleThreadExecutor()
    operator fun invoke(str: String){
        executor.execute {
            repository.editSharePreferences(str)
        }
    }
}