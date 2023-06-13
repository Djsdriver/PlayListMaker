package com.example.playlistmaker.settings.domain.usecase

import com.example.playlistmaker.settings.domain.repository.SettingsRepository

class IsDarkThemeEnabled(private val settingsRepository: SettingsRepository) {

    fun isDarkEnabled(): Boolean{
       return settingsRepository.isDarkThemeEnabled()
    }
}