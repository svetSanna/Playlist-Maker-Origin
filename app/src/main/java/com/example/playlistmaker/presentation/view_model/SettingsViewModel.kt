package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator

class SettingsViewModel : ViewModel() {
    private val sharedPreferencesInteractor = Creator.provideSharedPreferencesInteractor()

    fun editTheme(checked: String){
        sharedPreferencesInteractor.edit(checked)
    }
}