package com.example.playlistmaker.di

import com.example.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.repository.SettingsRepository
import com.example.playlistmaker.settings.domain.usecase.ChangeAppTheme
import com.example.playlistmaker.settings.domain.usecase.IsDarkThemeEnabled
import com.example.playlistmaker.settings.ui.SettingViewModel
import com.example.playlistmaker.sharing.domain.usecase.OpenTerms
import com.example.playlistmaker.sharing.domain.usecase.SendToSupport
import com.example.playlistmaker.sharing.domain.usecase.ShareApp
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingModule= module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(context = get())
    }

    factory { ChangeAppTheme(settingsRepository = get()) }
    factory { IsDarkThemeEnabled(settingsRepository = get()) }

    viewModel { SettingViewModel(
        openTerms = get(),
        sendToSupport = get(),
        shareApp = get(),
        changeAppTheme = get(),
        isDarkThemeEnabled = get()) }
}