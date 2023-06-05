package com.example.playlistmaker.settings.domain.usecase

import com.example.playlistmaker.settings.domain.repository.SettingsRepository

class ChangeAppTheme(private val settingsRepository: SettingsRepository) {

    fun changeAppTheme(current: Boolean){
        settingsRepository.changeAppTheme(current)
    }

}