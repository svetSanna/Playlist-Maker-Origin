package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.use_case.SharedPreferencesInteractor

class SettingsViewModel(private val sharedPreferencesInteractor : SharedPreferencesInteractor) : ViewModel() {
    //private val sharedPreferencesInteractor = Creator.provideSharedPreferencesInteractor()

    fun editTheme(checked: String){
        sharedPreferencesInteractor.edit(checked)
    }
}