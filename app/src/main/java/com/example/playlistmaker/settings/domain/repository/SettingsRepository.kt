package com.example.playlistmaker.settings.domain.repository

interface SettingsRepository {

    fun changeAppTheme(currentThemeIsDark: Boolean)

    fun isDarkThemeEnabled(): Boolean

}