package com.example.playlistmaker.utility

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.utility.Const.PRACTICUM_EXAMPLE_PREFERENCES
import com.example.playlistmaker.utility.Const.SWITCH_KEY

class App : Application() {

    var darkTheme = false


    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        val isTurn = sharedPrefs.getBoolean(SWITCH_KEY, false)
        switchTheme(isTurn)
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
