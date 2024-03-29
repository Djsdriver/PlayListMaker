package com.example.playlistmaker.settings.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val context:Context): SettingsRepository {
    private var darkTheme = false

    private val sharedPrefs by lazy {
        context.getSharedPreferences(
            Const.PRACTICUM_EXAMPLE_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
    }


    override fun changeAppTheme(currentThemeIsDark: Boolean) {
        darkTheme = currentThemeIsDark
        sharedPrefs.edit { putBoolean(Const.SWITCH_KEY, darkTheme) }
    }

    override fun isDarkThemeEnabled(): Boolean {
        val isTurn=sharedPrefs.getBoolean(Const.SWITCH_KEY,false)
        return isTurn
    }
}