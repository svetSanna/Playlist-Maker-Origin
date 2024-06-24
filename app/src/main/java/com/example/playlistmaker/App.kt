package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

const val PLAYLISTMAKER_PREFERENCES = "playlist_preferences" // ключ для SharedPreferences
const val THEME_SWITCH_KEY = "key_for_selectorSwitch" // ключ для перелючателя темы
const val SEARCH_HISTORY_KEY = "key_for_search_hystory" // ключ для истории поиска

class App : Application() {
    var darkTheme: Boolean = false
        get() {
            return field
        }
        set(value) {
            field = value
        }

    override fun onCreate() {

        // Получаем тему приложения, выбранную пользователем, из SharedPreferences, а если
        // ничего туда не успели сохранить, то применим текущую тему приложения
        val sharedPrefs = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)
        // MODE_PRIVATE - чтобы данные были досутпны только нашему приложению
        var stringFromSharedPrefs = sharedPrefs.getString(THEME_SWITCH_KEY, "")
        when (stringFromSharedPrefs) {
            "false" -> darkTheme = false
            "true" -> darkTheme = true
            "" -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> darkTheme = true
                    Configuration.UI_MODE_NIGHT_NO -> darkTheme = false
                }
            }
        }
        switchTheme(darkTheme)
        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}