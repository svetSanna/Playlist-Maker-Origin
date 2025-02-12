package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.use_case.SharedPreferencesInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.Locale

const val PLAYLISTMAKER_PREFERENCES = "playlist_preferences" // ключ для SharedPreferences
const val THEME_SWITCH_KEY = "key_for_selectorSwitch" // ключ для перелючателя темы
const val SEARCH_HISTORY_KEY = "key_for_search_hystory" // ключ для истории поиска

//const val TRACK =
//    "TRACK" // ключ для сериализации трека при передаче из SearchActivity в MediaActivity

class App : Application() {
    var darkTheme: Boolean = false
        get() {
            return field
        }
        set(value) {
            field = value
        }

    companion object {
        // функция отпределяет формы слова "трек" в зависимости от числительного n
        fun getEndingTrack(n: Int) : String{
            var s = n.toString()
            val currentLanguage = Locale.getDefault().displayLanguage
            if (currentLanguage.lowercase(Locale.getDefault()).contains("русский")) {
                if (s.endsWith("5") || s.endsWith("6") || s.endsWith("7") || s.endsWith("8") || s.endsWith("9")
                    || s.endsWith("0") || s.endsWith("11") || s.endsWith("12") || s.endsWith("13") || s.endsWith("14")
                    || s.endsWith("15") || s.endsWith("16") || s.endsWith("17") || s.endsWith("18") || s.endsWith("19")) return " треков"//"ов"
                if (s.endsWith("2") || s.endsWith("3") || s.endsWith("4")) return " трека"//"а"
                return " трек"//""
            }else{
                if(n==1) return " track"
                else return " tracks"
            }
        }

        // функция отпределяет формы слова "минута" в зависимости от числительного n
        fun getEndingMinute(n: Int) : String{
            var s = n.toString()

            val currentLanguage = Locale.getDefault().displayLanguage
            if (currentLanguage.lowercase(Locale.getDefault()).contains("русский")) {
                if (s.endsWith("5") || s.endsWith("6") || s.endsWith("7") || s.endsWith("8") || s.endsWith("9")
                    || s.endsWith("0") || s.endsWith("11") || s.endsWith("12") || s.endsWith("13") || s.endsWith("14")
                    || s.endsWith("15") || s.endsWith("16") || s.endsWith("17") || s.endsWith("18") || s.endsWith("19")) return " минут" //""
                if (s.endsWith("2") || s.endsWith("3") || s.endsWith("4")) return " минуты"//"ы"
                return " минута"//"а"
            } else{
                if(n==1) return " minute"
                else return " minutes"
            }
        }
    }
    override fun onCreate() {
        // Получаем тему приложения, выбранную пользователем, из SharedPreferences, а если
        // ничего туда не успели сохранить, то применим текущую тему приложения

        // Функция, которая настраивает библиотеку Koin, нужно вызвать перед использованием
        startKoin {
            androidLogger(Level.DEBUG)
            // Метод специального класса, переданного как this, для добавления контекста в граф
            androidContext(this@App)
            // Передаём все модули, чтобы их содержимое было передано в граф
            modules(listOf(dataModule, domainModule, viewModelModule)) // (listOf(dataModule,...))
        }

        var stringFromSharedPrefs = getKoin().get<SharedPreferencesInteractor>().getString(THEME_SWITCH_KEY)

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